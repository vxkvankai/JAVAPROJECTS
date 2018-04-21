import java.util.concurrent.atomic.AtomicLong

/**
 * D3Banking
 * User: lpresswood
 * Date: 10/23/14
 * Time: 3:20 PM
 *
 *
 */
class D3Transaction {

  // Global Counter to deal with our seed data
    static final AtomicLong sourceTxIdCounter = new AtomicLong(0)


    java.sql.Date createTs
    short deleted=0
    BigDecimal amount
    short offline=0
    short pending=0
    def sourceTxId
    int type =  1
    long accountId
    long categoryId
    String memo


  public D3Transaction(BigDecimal amount, def accountId,
                def categoryId){
    this.createTs = new java.sql.Date(new Date().getTime());
    this.amount = amount
    UUID uuid = UUID.randomUUID()

    this.sourceTxId = uuid.toString().replace("-","")
    this.memo = "PERF-" + sourceTxIdCounter.incrementAndGet()
    this.accountId = accountId
    this.categoryId = categoryId
  }


    @Override
    public String toString() {
        return "D3Transaction{" +
                "createTs=" + createTs +
                ", deleted=" + deleted +
                ", amount=" + amount +
                ", offline=" + offline +
                ", pending=" + pending +
                ", sourceTxId=" + sourceTxId +
                ", type=" + type +
                ", accountId=" + accountId +
                ", categoryId=" + categoryId +
                ", memo='" + memo + '\'' +
                '}';
    }
}
