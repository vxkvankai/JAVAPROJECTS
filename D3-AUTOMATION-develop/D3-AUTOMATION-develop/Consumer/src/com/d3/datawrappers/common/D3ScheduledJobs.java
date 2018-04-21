package com.d3.datawrappers.common;

public enum D3ScheduledJobs {
    ACH_CLEANUP_FILE("achCleanupFileJob"),
    ACH_GENERATE_A2A_FILE("achGenerateA2aFileJob"),
    ACH_GENERATE_CCD_FILE("achGenerateCcdFileJob"),
    ACH_GENERATE_CIE_FILE("achGenerateCieFileJob"),
    ACH_GENERATE_P2P_FILE("achGenerateP2pFileJob"),
    ACH_GENERATE_PPD_FILE("achGeneratePpdFileJob"),
    ACH_GENERATE_WEB_FILE("achGenerateWebFileJob"),
    ACH_GENERATE_ZEL_FILE("achGenerateZelFileJob"),
    ACH_PRE_NOTIFICATION("achPreNotificationJob"),
    ACH_TRANSFER("achTransferJob"),
    ACH_ZEL_SETTLEMENT("achZelSettlementJob"),
    AUTO_PROMOTION("autoPromotionJob"),
    CASH_EDGE_UPDATE("cashEdgeUpdateJob"),
    CLEANUP("cleanupJob"),
    CONDUIT_FILE_CLEANUP("conduitFileCleanupJob"),
    D3_INTERNAL_TRANSFER("d3InternalTransferJob"),
    D3_ON_US_TRANSFER("d3OnUsTransferJob"),
    DAILY_ALERTS("dailyAlertsJob"),
    FEDWIRE_CLEANUP_FILE("fedwireCleanupFileJob"),
    FEDWIRE_CLEANUP_STATS("fedwireCleanupStatsJob"),
    FEDWIRE_GENERATE_FILE("fedwireGenerateFileJob"),
    FEDWIRE_TRANSFER("fedwireTransferJob"),
    FEE_BILLING_DAILY_UPDATE("feeBillingDailyUpdateJob"),
    FEE_BILLING_PERIODIC_REPORT("feeBillingPeriodicReportJob"),
    MASK_UPDATE("maskUpdateJob"),
    MASTER("masterJob"),
    METRIC_EVENT_ROLLUP("metricEventRollupJob"),
    SCHEDULED_ALERTS("scheduledAlertsJob"),
    USER_CLEANUP_NOTIFIER("userCleanupNotifierJob"),
    ZELLE_TRANSFER("zelleTransferJob");

    String jobName;

    D3ScheduledJobs(String jobName) {
        this.jobName = jobName;
    }

    public String getJobName() {
        return jobName;
    }
}
