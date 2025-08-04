import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

public class DepositDataGenerator {
    private static final Random random = new Random();
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
    private static final String[] CUSTOMER_PREFIX = {"P", "C", "G", "F", "I", "O", "S", "B", "T", "E"};
    private static final String[] CURRENCY_CODES = {"001", "002", "003", "004", "005", "006", "007", "008", "009", "010",
            "011", "012", "013", "014", "015", "016", "017", "018", "019", "020", "099"};

    public static void main(String[] args) {
        // 生成数据
        List<Map<String, String>> mainTableData = generateMainTableData(100);
        List<Map<String, String>> extTableData = generateExtTableData(mainTableData);

        // 写入CSV文件
        writeToCSV(mainTableData, "存款主表.csv", getMainTableHeaders());
        writeToCSV(extTableData, "存款账户拓展信息表.csv", getExtTableHeaders());

        System.out.println("CSV文件生成完成！");
    }

    // 生成主表数据
    private static List<Map<String, String>> generateMainTableData(int count) {
        List<Map<String, String>> data = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            Map<String, String> record = new LinkedHashMap<>();

            // 生成机构号+账户号作为KEY_1
            String branchNo = String.format("%03d", random.nextInt(900) + 100);
            String accountNo = String.format("%013d", i + 1);
            String key1 = branchNo + accountNo;
            record.put("KEY_1", key1);

            // 其他字段
            record.put("TRAP", String.valueOf(random.nextInt(12) + 1)); // 付息方式 1-12
            record.put("INVM_TRM_RLLOVR_TY", String.valueOf(random.nextInt(5))); // 自动转存标识 0-4
            record.put("INVM_RATE_IND", String.valueOf(random.nextInt(7))); // 到期标识 0-6
            record.put("STMT_FREQUENCY", String.valueOf(random.nextInt(9) + 1)); // 对账单周期 1-9
            record.put("STMT_CYCLE", String.valueOf(random.nextInt(11) + 1)); // 对账单周期基期 1-11
            record.put("STOP_WDL_IND", String.valueOf(random.nextInt(9))); // 止付标志 0-8
            record.put("OMNI_FLAG", String.valueOf(random.nextInt(7))); // 综存标识 0-6
            record.put("INT_RT_CHNG_ST_IND", String.valueOf(random.nextInt(9))); // 利率维护标志 0-8
            record.put("SWEEP_ACCT_FLAG", String.valueOf(random.nextInt(2))); // 是否允许SWEEP功能 0-1
            record.put("STMT_DAY", String.valueOf(random.nextInt(10) + 1)); // 对帐单日 1-10
            record.put("NEG_RATES_FLAG", String.valueOf(random.nextInt(9) + 1)); // 利率执行方式 1-9
            record.put("CAS_CHQ_STOP_IND", String.valueOf(random.nextInt(9))); // 支票账户止付标志 0-8
            record.put("LIM_CNTRL_FLAG_CAP", getRandomValue(new String[]{"M", "S", "A", "U", "C", "V", " "})); // 账户类型标识
            record.put("LST_CUST_CR_DT", getRandomDate());
            record.put("LST_CUST_DR_DT", getRandomDate());
            record.put("CURR_BAL", String.format("%.2f", random.nextDouble() * 1000000)); // 余额
            record.put("HOLD_VAL", String.format("%.2f", random.nextDouble() * 100000)); // 冻结金额
            record.put("INVM_TRM_VALUE", String.format("%.2f", random.nextDouble() * 500000)); // 定期金额
            record.put("INT_ADJUSTMENT", String.format("%.2f", random.nextDouble() * 1000)); // 利息调整值
            record.put("CUSTOMER_NO", generateCustomerNo()); // 客户号
            record.put("ACCT_TYPE", String.valueOf(random.nextInt(15) + 1)); // 账户类型 1-15
            record.put("BRANCH_NO", branchNo); // 机构号
            record.put("ACCT_OPEN_DT", getRandomDate());
            record.put("INT_FRM_DT", getRandomDate());
            record.put("INT_TO_DT", getRandomDate());
            record.put("INVM_MAT_DT", getRandomDate());
            record.put("CURR_STATUS", String.valueOf(random.nextInt(13))); // 账户状态 0-12
            record.put("CURRENCY", CURRENCY_CODES[random.nextInt(CURRENCY_CODES.length)]); // 币别
            record.put("LST_FIN_DT", getRandomDate());
            record.put("VAR_INT_RATE", String.format("%.4f", random.nextDouble() * 10)); // 账户层执行利率
            record.put("CR_STORE_RATE", String.format("%.4f", random.nextDouble() * 5)); // 账户贷记利率
            record.put("GL_CLASS_CODE", String.valueOf((random.nextInt(9) + 1) * 1000)); // GL分类代码 1000-9000
            record.put("INVM_TERM_BASIS", String.valueOf(random.nextInt(9) + 1)); // 存期单位 1-9
            record.put("INVM_TRM_DAY", String.valueOf(random.nextInt(3650) + 1)); // 存期长度
            record.put("FIL13", "备注信息" + (i + 1)); // 备注
            record.put("TRM_INT_ACCR", String.format("%.2f", random.nextDouble() * 5000)); // 大额存单可用利息
            record.put("ITYPE_INT_ACCR", String.format("%.2f", random.nextDouble() * 1000)); // 大额存单利息调整值
            record.put("DIV1", String.format("%.2f", random.nextDouble() * 1000)); // 第一次付息金额
            record.put("DIV2", String.format("%.2f", random.nextDouble() * 1000)); // 第二次付息金额
            record.put("DIV3", String.format("%.2f", random.nextDouble() * 1000)); // 第三次付息金额
            record.put("DIV4", String.format("%.2f", random.nextDouble() * 1000)); // 第四次付息金额
            record.put("DIV5", String.format("%.2f", random.nextDouble() * 1000)); // 第五次付息金额
            record.put("NO_OF_HOLDS", String.valueOf(random.nextInt(10))); // 冻结次数
            record.put("NO_OF_NPBS", String.valueOf(random.nextInt(20))); // 未登折交易数
            record.put("NO_OF_STOPS", String.valueOf(random.nextInt(10))); // 止付次数
            record.put("PB_STATUS", String.valueOf(random.nextInt(10))); // 存折状态 0-9
            record.put("CHOP_STATUS", String.valueOf(random.nextInt(10))); // 芯片状态 0-9
            record.put("FUND_TYPE", String.valueOf(random.nextInt(12) + 1)); // 外币资金类型 1-12
            record.put("FCY_ITEM", String.valueOf(random.nextInt(17) + 1)); // 外汇账户项目类型 1-17
            record.put("FCY_CLASS", String.valueOf(random.nextInt(15) + 1)); // 外汇账户类别 1-15
            record.put("DIWW_EDU_BRK_FLG", String.valueOf(random.nextInt(15) + 1)); // 违约类型 1-15
            record.put("INSTRU_CNT", String.valueOf(random.nextInt(10) + 1)); // 重空关联类型 1-10
            record.put("AGMT_FLAG", String.valueOf(random.nextInt(9))); // 签约标志 0-8
            record.put("PRIM_ACCT_FLG", String.valueOf(random.nextInt(7))); // 主账户标志 0-6
            record.put("EMBOSS_FLAG", String.valueOf(random.nextInt(4))); // 允许转久悬标志 0-3
            record.put("CARD_PRIM_ACCT_IND", String.valueOf(random.nextInt(7))); // 卡内主账户标志 0-6
            record.put("PB_NUMBER", "PB" + String.format("%08d", i + 1)); // 存折号
            record.put("FCY_RANGE", String.valueOf(random.nextInt(15) + 1)); // 外汇账户收支范围 1-15
            record.put("NTC_LST_NTC_CNT", String.valueOf(random.nextInt(15) + 1)); // 最新通知编号 1-15
            record.put("NEGOED_INT_RATE_EXEC_MODE", String.valueOf(random.nextInt(10) + 1)); // 协定利率执行方式 1-10
            record.put("NEGOED_EXEC_INT_RATE", String.format("%.6f", random.nextDouble() * 8)); // 协定执行利率
            record.put("NEGOED_INT_RATE_FLOAT", String.format("%.6f", random.nextDouble() * 2)); // 协定利率浮动值
            record.put("SWINDLE_STAT", String.valueOf(random.nextInt(9))); // 天添利签约标识 0-8
            record.put("FIL22", String.valueOf(random.nextInt(15) + 1)); // 身份验证代码 1-15
            record.put("SKLI_POID", String.valueOf(random.nextInt(31) + 100)); // 外围系统代码 100-130
            record.put("OD_VISA_AREA_1", String.valueOf(random.nextInt(9) + 1)); // 透支账户信息 1-9
            record.put("DR_INT_ADJUSTMENT", String.format("%.5f", random.nextDouble() * 1000)); // 利息调整
            record.put("ACCT_TYPE_CHG_FLAG", String.valueOf(random.nextInt(2) + 1)); // 现管签约标识 1-2
            record.put("MCURR_ACCT_TYPE", String.valueOf(random.nextInt(12))); // 日日盈签约标识 0-11

            data.add(record);
        }

        return data;
    }

    // 生成拓展表数据（基于主表数据，确保重复字段一致）
    private static List<Map<String, String>> generateExtTableData(List<Map<String, String>> mainTableData) {
        List<Map<String, String>> data = new ArrayList<>();

        for (Map<String, String> mainRecord : mainTableData) {
            Map<String, String> record = new LinkedHashMap<>();

            // 从主表复制相同的字段
            record.put("KEY_1", mainRecord.get("KEY_1"));
            record.put("PB_STATUS", mainRecord.get("PB_STATUS"));
            record.put("CHOP_STATUS", mainRecord.get("CHOP_STATUS"));
            record.put("FUND_TYPE", mainRecord.get("FUND_TYPE"));
            record.put("FCY_ITEM", mainRecord.get("FCY_ITEM"));
            record.put("FCY_CLASS", mainRecord.get("FCY_CLASS"));
            record.put("DIWW_EDU_BRK_FLG", mainRecord.get("DIWW_EDU_BRK_FLG"));
            record.put("INSTRU_CNT", mainRecord.get("INSTRU_CNT"));
            record.put("AGMT_FLAG", mainRecord.get("AGMT_FLAG"));
            record.put("PRIM_ACCT_FLG", mainRecord.get("PRIM_ACCT_FLG"));
            record.put("EMBOSS_FLAG", mainRecord.get("EMBOSS_FLAG"));
            record.put("CARD_PRIM_ACCT_IND", mainRecord.get("CARD_PRIM_ACCT_IND"));
            record.put("PB_NUMBER", mainRecord.get("PB_NUMBER"));
            record.put("FCY_RANGE", mainRecord.get("FCY_RANGE"));
            record.put("NTC_LST_NTC_CNT", mainRecord.get("NTC_LST_NTC_CNT"));
            record.put("NEGOED_INT_RATE_EXEC_MODE", mainRecord.get("NEGOED_INT_RATE_EXEC_MODE"));
            record.put("NEGOED_EXEC_INT_RATE", mainRecord.get("NEGOED_EXEC_INT_RATE"));
            record.put("NEGOED_INT_RATE_FLOAT", mainRecord.get("NEGOED_INT_RATE_FLOAT"));
            record.put("SWINDLE_STAT", mainRecord.get("SWINDLE_STAT"));
            record.put("SKLI_POID", mainRecord.get("SKLI_POID"));

            // 拓展表特有字段
            record.put("EMBOSS_FLAG", String.valueOf(random.nextInt(4))); // 是否允许转久悬 0-3
            record.put("TD_INST_AMT", String.format("%.2f", random.nextDouble() * 100000)); // 分期存入金额
            record.put("TEMP_ACCT_EXP_DT", getRandomDate()); // 临时账户到期日
            record.put("ANNL_REVW_DATE", getRandomDate()); // 公司帐户年审日
            record.put("FREEZE_EXP_DATE", getRandomDate()); // 冻结到期日
            record.put("CCDA_VSTRO_LOW_RT", String.format("%.6f", random.nextDouble() * 5)); // 提前解约利率
            record.put("NRDA_THRSHLD_AMT", String.format("%.2f", random.nextDouble() * 50000)); // 协定存款协定金额
            record.put("FIRST_DEP_IBD_DT", getRandomDate()); // 第一次存款日期
            record.put("NTC_CNCL_INT_PEN", String.format("%.2f", random.nextDouble() * 1000)); // 通知撤销罚息
            record.put("ADB_SUM", String.format("%.2f", random.nextDouble() * 100000)); // 平均余额
            record.put("WDL_DEP_FREQ", String.valueOf(random.nextInt(13) + 1)); // 存款频率 1-13
            record.put("WDL_DEP_FREQ_BAS", String.valueOf(random.nextInt(11) + 1)); // 频率基期 1-11
            record.put("BAL_BFR_BREAK", String.format("%.2f", random.nextDouble() * 50000)); // 违约前余额
            record.put("DIWW_EDU_BRK_DT", getRandomDate()); // 违约日期
            record.put("SAVE_BREAK_INT", String.format("%.2f", random.nextDouble() * 1000)); // 违约活期利息
            record.put("SP_HOLD_VALUE", String.format("%.2f", random.nextDouble() * 5000)); // 支付宝冻结金额
            record.put("SRC_OF_FUND_CODE", String.valueOf(random.nextInt(15) + 1)); // 账户用途 1-15
            record.put("CHQ_PASWD_TYPE", String.valueOf(random.nextInt(9) + 1)); // 票据密码方式 1-9
            record.put("FIL01", "扩展字段" + (data.size() + 1)); // 扩展字段

            data.add(record);
        }

        return data;
    }

    // 生成随机日期
    private static String getRandomDate() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, -random.nextInt(365 * 5));
        return dateFormat.format(cal.getTime());
    }

    // 生成客户号
    private static String generateCustomerNo() {
        String prefix = CUSTOMER_PREFIX[random.nextInt(CUSTOMER_PREFIX.length)];
        return prefix + String.format("%08d", random.nextInt(100000000));
    }

    // 从数组中随机选择一个值
    private static String getRandomValue(String[] values) {
        return values[random.nextInt(values.length)];
    }

    // 获取主表CSV头
    private static String[] getMainTableHeaders() {
        return new String[]{
                "KEY_1", "TRAP", "INVM_TRM_RLLOVR_TY", "INVM_RATE_IND", "STMT_FREQUENCY", "STMT_CYCLE",
                "STOP_WDL_IND", "OMNI_FLAG", "INT_RT_CHNG_ST_IND", "SWEEP_ACCT_FLAG", "STMT_DAY",
                "NEG_RATES_FLAG", "CAS_CHQ_STOP_IND", "LIM_CNTRL_FLAG_CAP", "LST_CUST_CR_DT",
                "LST_CUST_DR_DT", "CURR_BAL", "HOLD_VAL", "INVM_TRM_VALUE", "INT_ADJUSTMENT",
                "CUSTOMER_NO", "ACCT_TYPE", "BRANCH_NO", "ACCT_OPEN_DT", "INT_FRM_DT", "INT_TO_DT",
                "INVM_MAT_DT", "CURR_STATUS", "CURRENCY", "LST_FIN_DT", "VAR_INT_RATE", "CR_STORE_RATE",
                "GL_CLASS_CODE", "INVM_TERM_BASIS", "INVM_TRM_DAY", "FIL13", "TRM_INT_ACCR",
                "ITYPE_INT_ACCR", "DIV1", "DIV2", "DIV3", "DIV4", "DIV5", "NO_OF_HOLDS", "NO_OF_NPBS", "NO_OF_STOPS", "PB_STATUS", "CHOP_STATUS",
                "FUND_TYPE", "FCY_ITEM", "FCY_CLASS", "DIWW_EDU_BRK_FLG", "INSTRU_CNT",
                "AGMT_FLAG", "PRIM_ACCT_FLG", "EMBOSS_FLAG", "CARD_PRIM_ACCT_IND", "PB_NUMBER",
                "FCY_RANGE", "NTC_LST_NTC_CNT", "NEGOED_INT_RATE_EXEC_MODE", "NEGOED_EXEC_INT_RATE",
                "NEGOED_INT_RATE_FLOAT", "SWINDLE_STAT", "FIL22", "SKLI_POID", "OD_VISA_AREA_1",
                "DR_INT_ADJUSTMENT", "ACCT_TYPE_CHG_FLAG", "MCURR_ACCT_TYPE"
        };
    }

    // 获取拓展表CSV头
    private static String[] getExtTableHeaders() {
        return new String[]{
                "KEY_1", "PB_STATUS", "CHOP_STATUS", "EMBOSS_FLAG", "PB_NUMBER", "FUND_TYPE",
                "TD_INST_AMT", "TEMP_ACCT_EXP_DT", "ANNL_REVW_DATE", "FREEZE_EXP_DATE",
                "CCDA_VSTRO_LOW_RT", "FCY_ITEM", "FCY_CLASS", "FCY_RANGE", "NRDA_THRSHLD_AMT",
                "PRIM_ACCT_FLG", "FIRST_DEP_IBD_DT", "INSTRU_CNT", "NTC_CNCL_INT_PEN", "ADB_SUM",
                "NTC_LST_NTC_CNT", "MAT_INT_AVAIL", "WDL_DEP_FREQ", "WDL_DEP_FREQ_BAS",
                "BAL_BFR_BREAK", "DIWW_EDU_BRK_FLG", "DIWW_EDU_BRK_DT", "SAVE_BREAK_INT",
                "AGMT_FLAG", "CARD_PRIM_ACCT_IND", "SP_HOLD_VALUE", "NEGOED_INT_RATE_EXEC_MODE",
                "NEGOED_EXEC_INT_RATE", "NEGOED_INT_RATE_FLOAT", "SWINDLE_STAT", "SKLI_POID",
                "SRC_OF_FUND_CODE", "CHQ_PASWD_TYPE", "FIL01"
        };
    }

    // 写入CSV文件
    private static void writeToCSV(List<Map<String, String>> data, String fileName, String[] headers) {
        try (FileWriter writer = new FileWriter(fileName)) {
            // 写入表头
            writer.write(String.join(",", headers) + "\n");

            // 写入数据
            for (Map<String, String> record : data) {
                List<String> values = new ArrayList<>();
                for (String header : headers) {
                    values.add(record.getOrDefault(header, ""));
                }
                writer.write(String.join(",", values) + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}