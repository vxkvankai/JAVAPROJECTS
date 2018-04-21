package com.d3.api.mappings.users;

import com.d3.api.mappings.accounts.Account;
import com.d3.datawrappers.account.D3Account;
import com.d3.datawrappers.user.D3AccountLimits;
import com.d3.datawrappers.user.D3SecondaryUser;
import com.d3.datawrappers.user.enums.AccountPermissions;
import com.d3.datawrappers.user.enums.UserServices;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class AccountServiceGroups {

    private AccountGroup accountGroup;
    private List<ServiceGroup> serviceGroup;

    private AccountServiceGroups(AccountGroup accountGroup, List<ServiceGroup> serviceGroup) {
        this.accountGroup = accountGroup;
        this.serviceGroup = serviceGroup;
    }

    /**
     * Sets Access Levels for Secondary User based on User Services they have access to
     *
     * @param secondaryUser Secondary User being created
     * @return List <ServiceGroup> with access levels set for Secondary User
     */
    public List<ServiceGroup> setServiceAccessLevel(D3SecondaryUser secondaryUser) {
        List<ServiceGroup> serviceGroups = serviceGroup;
        serviceGroups.forEach(group -> {
            List<Service> availableServices = group.getServices();
            List<String> userServiceAccess = secondaryUser.getServices().stream().map(UserServices::getServiceL10nKey).collect(Collectors.toList());
            availableServices.forEach(service -> {
                if (userServiceAccess.contains(service.getL10nKey())) {
                    service.setAccess(service.getAvailablePermissions());
                }
            });
        });
        return serviceGroups;
    }


    /**
     * Sets Account access levels and limits for Secondary User based on D3Accounts they have access to
     *
     * @param secondaryUser Secondary user being created
     * @return AccountGroup with account access levels and limits set
     */
    public AccountGroup setAccountAccessAndLimits(D3SecondaryUser secondaryUser) {
        AccountGroup availableAccountGroup = accountGroup;
        List<D3Account> secondaryUserAccounts = secondaryUser.getAccounts();
        for (Account account : availableAccountGroup.getAccounts()) {
            for (D3Account userAccount : secondaryUserAccounts) {
                if (userAccount.getName().equals(account.getName())) {
                    List<String> accessLevels = secondaryUser.getAccountPermissions(userAccount).stream().map(AccountPermissions::name).collect(Collectors.toList());
                    account.setAccess(accessLevels);
                    secondaryUser.getAccountLimitsForAccount(userAccount).forEach(limit -> setLimits(account, limit));
                }
            }
        }
        return availableAccountGroup;
    }

    /**
     * Sets account limits based on the permission type (ACH, BILL_PAY, TRANSFER, WIRE) for the given Account if the account has access
     *
     * @param account Account to set limits for
     * @param limit D3AccountLimit
     */
    private void setLimits(Account account, D3AccountLimits limit) {
        switch (limit.getPermission()) {
            case ACH:
                if (account.getHasAchAccess()) {
                    account.setAchLimit(new AchLimit(limit));
                }
                break;
            case BILL_PAY:
                if (account.getHasBillPayAccess()) {
                    account.setBillPayLimit(new BillPayLimit(limit));
                }
                break;
            case READ:
                // TODO: Add permission
                break;
            case STATEMENTS:
                // TODO: Add permission
                break;
            case TRANSACTIONS:
                // TODO: Add permission
                break;
            case TRANSFER:
                if (account.getHasTransferAccess()) {
                    account.setTransferLimit(new TransferLimit(limit));
                }
                break;
            case WIRE:
                if (account.getHasWireAccess()) {
                    account.setWireLimit(new WireLimit(limit));
                }
                break;
        }
    }

    /**
     * The default retrofit converter wasn't parsing the {usermgmt/users/new} @GET call. This deserializer will parse the Account Groups and Service Groups
     * Objects from the JSON and return the correct object types
     */
    public static final Gson deserializer = new GsonBuilder()
        .registerTypeAdapter(AccountServiceGroups.class, (JsonDeserializer<AccountServiceGroups>) (json, typeOfT, context) -> {
            JsonObject accountGroupJsonObject = json.getAsJsonObject().getAsJsonArray("accountGroups").getAsJsonArray().get(0).getAsJsonObject();
            AccountGroup accountGroups = new AccountGroup();
            accountGroups.setName(accountGroupJsonObject.get("name").getAsString());
            accountGroups.setProfileType(accountGroupJsonObject.get("profileType").getAsString());
            accountGroups.setAccounts(new Gson().fromJson(accountGroupJsonObject.getAsJsonArray("accounts"), new TypeToken<ArrayList<Account>>() {
            }.getType()));

            JsonArray serviceGroupsJsonArray = json.getAsJsonObject().getAsJsonArray("serviceGroups");
            ArrayList<JsonObject> serviceGroupJsonObject = new ArrayList<>();
            serviceGroupsJsonArray.forEach(group -> serviceGroupJsonObject.add(group.getAsJsonObject()));

            ArrayList<ServiceGroup> serviceGroups = new ArrayList<>();
            serviceGroupJsonObject.forEach(jsonObject -> {
                ServiceGroup newServiceGroup = new ServiceGroup();
                newServiceGroup.setDisplayOrder(jsonObject.get("displayOrder").getAsInt());
                newServiceGroup.setL10nKey(jsonObject.get("l10nKey").getAsString());
                newServiceGroup.setServices(new Gson().fromJson(jsonObject.getAsJsonArray("services"), new TypeToken<ArrayList<Service>>() {
                }.getType()));
                serviceGroups.add(newServiceGroup);
            });
            return new AccountServiceGroups(accountGroups, serviceGroups);
        })
        .create();
}
