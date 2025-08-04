//确保key_1不重复

import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

public class DepositDataGenerator_V2 {
    private static final Random random = new Random();
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
    private static final String[] CUSTOMER_PREFIX = {"P", "C", "G", "F", "I", "O", "S", "B", "T", "E"};
    private static final String[] CURRENCY_CODES = {"001", "002", "003", "004", "005", "006", "007", "008", "009", "010",
            "011", "012", "013", "014", "015", "016", "017", "018", "019", "020", "099"};
    private static final String QUOTE = "\"";
    private static final String DELIMITER = "\u001B"; // 0x1B作为分隔符
    private static final Set<String> generatedKey1Set = new HashSet<>(); // 用于存储已生成的KEY_1值

    public static void main(String[] args) {
        // 生成数据
        List<Map<String, String>> mainTableData = generateMainTableData(100000);
        List<Map<String, String>> extTableData = generateExtTableData(mainTableData);

        // 写入CSV文件
        writeToCSV(mainTableData, "ABCINVM.csv", getMainTableHeaders());
        writeToCSV(extTableData, "ABCINVE.csv", getExtTableHeaders());

        System.out.println("CSV文件生成完成！");
    }

    // 生成主表数据
    private static List<Map<String, String>> generateMainTableData(int count) {
        List<Map<String, String>> data = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            Map<String, String> record = new LinkedHashMap<>();

            // 生成机构号+账户号作为KEY_1，确保不重复
            String key1;
            do {
                // 随机生成3位机构号(100-999)
                String branchNo = String.format("%03d", random.nextInt(900) + 100);
                // 随机生成13位账户号(避免前导零)
                String accountNo = String.format("%013d",
                        (random.nextInt(9) + 1) * 1000000000000L +  // 确保第一位不是0
                                random.nextLong() % 1000000000000L);        // 随机12位
                key1 = branchNo + accountNo;
            } while (generatedKey1Set.contains(key1));
            generatedKey1Set.add(key1);
            record.put("KEY_1", quoteField(key1));

            // 其他字段
            record.put("TRAP", quoteField(String.valueOf(random.nextInt(12) + 1)));
            record.put("INVM_TRM_RLLOVR_TY", quoteField(String.valueOf(random.nextInt(5))));
            record.put("INVM_RATE_IND", quoteField(String.valueOf(random.nextInt(7))));
            record.put("STMT_FREQUENCY", quoteField(String.valueOf(random.nextInt(9) + 1)));
            record.put("STMT_CYCLE", quoteField(String.valueOf(random.nextInt(11) + 1)));
            record.put("STOP_WDL_IND", quoteField(String.valueOf(random.nextInt(9))));
            record.put("OMNI_FLAG", quoteField(String.valueOf(random.nextInt(7))));
            record.put("INT_RT_CHNG_ST_IND", quoteField(String.valueOf(random.nextInt(9))));
            record.put("SWEEP_ACCT_FLAG", quoteField(String.valueOf(random.nextInt(2))));
            record.put("STMT_DAY", quoteField(String.valueOf(random.nextInt(10) + 1)));
            record.put("NEG_RATES_FLAG", quoteField(String.valueOf(random.nextInt(9) + 1)));
            record.put("CAS_CHQ_STOP_IND", quoteField(String.valueOf(random.nextInt(9))));
            record.put("LIM_CNTRL_FLAG_CAP", quoteField(getRandomValue(new String[]{"M", "S", "A", "U", "C", "V", " "})));
            record.put("LST_CUST_CR_DT", quoteField(getRandomDate()));
            record.put("LST_CUST_DR_DT", quoteField(getRandomDate()));
            record.put("CURR_BAL", quoteField(String.format("%.2f", random.nextDouble() * 1000000)));
            record.put("HOLD_VAL", quoteField(String.format("%.2f", random.nextDouble() * 100000)));
            record.put("INVM_TRM_VALUE", quoteField(String.format("%.2f", random.nextDouble() * 500000)));
            record.put("INT_ADJUSTMENT", quoteField(String.format("%.2f", random.nextDouble() * 1000)));
            record.put("CUSTOMER_NO", quoteField(generateCustomerNo()));
            record.put("ACCT_TYPE", quoteField(String.valueOf(random.nextInt(15) + 1)));
            record.put("BRANCH_NO", quoteField(key1.substring(0, 3)));
            record.put("ACCT_OPEN_DT", quoteField(getRandomDate()));
            record.put("INT_FRM_DT", quoteField(getRandomDate()));
            record.put("INT_TO_DT", quoteField(getRandomDate()));
            record.put("INVM_MAT_DT", quoteField(getRandomDate()));
            record.put("CURR_STATUS", quoteField(String.valueOf(random.nextInt(13))));
            record.put("CURRENCY", quoteField(CURRENCY_CODES[random.nextInt(CURRENCY_CODES.length)]));
            record.put("LST_FIN_DT", quoteField(getRandomDate()));
            record.put("VAR_INT_RATE", quoteField(String.format("%.4f", random.nextDouble() * 10)));
            record.put("CR_STORE_RATE", quoteField(String.format("%.4f", random.nextDouble() * 5)));
            record.put("GL_CLASS_CODE", quoteField(String.valueOf((random.nextInt(9) + 1) * 1000)));
            record.put("INVM_TERM_BASIS", quoteField(String.valueOf(random.nextInt(9) + 1)));
            record.put("INVM_TRM_DAY", quoteField(String.valueOf(random.nextInt(3650) + 1)));
            record.put("FIL13", quoteField("备注信息" + (i + 1)));
            record.put("TRM_INT_ACCR", quoteField(String.format("%.2f", random.nextDouble() * 5000)));
            record.put("ITYPE_INT_ACCR", quoteField(String.format("%.2f", random.nextDouble() * 1000)));
            record.put("DIV1", quoteField(String.format("%.2f", random.nextDouble() * 1000)));
            record.put("DIV2", quoteField(String.format("%.2f", random.nextDouble() * 1000)));
            record.put("DIV3", quoteField(String.format("%.2f", random.nextDouble() * 1000)));
            record.put("DIV4", quoteField(String.format("%.2f", random.nextDouble() * 1000)));
            record.put("DIV5", quoteField(String.format("%.2f", random.nextDouble() * 1000)));
            record.put("NO_OF_HOLDS", quoteField(String.valueOf(random.nextInt(10))));
            record.put("NO_OF_NPBS", quoteField(String.valueOf(random.nextInt(20))));
            record.put("NO_OF_STOPS", quoteField(String.valueOf(random.nextInt(10))));
            record.put("PB_STATUS", quoteField(String.valueOf(random.nextInt(10))));
            record.put("CHOP_STATUS", quoteField(String.valueOf(random.nextInt(10))));
            record.put("FUND_TYPE", quoteField(String.valueOf(random.nextInt(12) + 1)));
            record.put("FCY_ITEM", quoteField(String.valueOf(random.nextInt(17) + 1)));
            record.put("FCY_CLASS", quoteField(String.valueOf(random.nextInt(15) + 1)));
            record.put("DIWW_EDU_BRK_FLG", quoteField(String.valueOf(random.nextInt(15) + 1)));
            record.put("INSTRU_CNT", quoteField(String.valueOf(random.nextInt(10) + 1)));
            record.put("AGMT_FLAG", quoteField(String.valueOf(random.nextInt(9))));
            record.put("PRIM_ACCT_FLG", quoteField(String.valueOf(random.nextInt(7))));
            record.put("EMBOSS_FLAG", quoteField(String.valueOf(random.nextInt(4))));
            record.put("CARD_PRIM_ACCT_IND", quoteField(String.valueOf(random.nextInt(7))));
            record.put("PB_NUMBER", quoteField("PB" + String.format("%08d", i + 1)));
            record.put("FCY_RANGE", quoteField(String.valueOf(random.nextInt(15) + 1)));
            record.put("NTC_LST_NTC_CNT", quoteField(String.valueOf(random.nextInt(15) + 1)));
            record.put("NEGOED_INT_RATE_EXEC_MODE", quoteField(String.valueOf(random.nextInt(10) + 1)));
            record.put("NEGOED_EXEC_INT_RATE", quoteField(String.format("%.6f", random.nextDouble() * 8)));
            record.put("NEGOED_INT_RATE_FLOAT", quoteField(String.format("%.6f", random.nextDouble() * 2)));
            record.put("SWINDLE_STAT", quoteField(String.valueOf(random.nextInt(9))));
            record.put("FIL22", quoteField(String.valueOf(random.nextInt(15) + 1)));
            record.put("SKLI_POID", quoteField(String.valueOf(random.nextInt(31) + 100)));
            record.put("OD_VISA_AREA_1", quoteField(String.valueOf(random.nextInt(9) + 1)));
            record.put("DR_INT_ADJUSTMENT", quoteField(String.format("%.5f", random.nextDouble() * 1000)));
            record.put("ACCT_TYPE_CHG_FLAG", quoteField(String.valueOf(random.nextInt(2) + 1)));
            record.put("MCURR_ACCT_TYPE", quoteField(String.valueOf(random.nextInt(12))));

            data.add(record);
        }

        return data;
    }

    // 生成拓展表数据
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
            record.put("EMBOSS_FLAG", quoteField(String.valueOf(random.nextInt(4))));
            record.put("TD_INST_AMT", quoteField(String.format("%.2f", random.nextDouble() * 100000)));
            record.put("TEMP_ACCT_EXP_DT", quoteField(getRandomDate()));
            record.put("ANNL_REVW_DATE", quoteField(getRandomDate()));
            record.put("FREEZE_EXP_DATE", quoteField(getRandomDate()));
            record.put("CCDA_VSTRO_LOW_RT", quoteField(String.format("%.6f", random.nextDouble() * 5)));
            record.put("NRDA_THRSHLD_AMT", quoteField(String.format("%.2f", random.nextDouble() * 50000)));
            record.put("FIRST_DEP_IBD_DT", quoteField(getRandomDate()));
            record.put("NTC_CNCL_INT_PEN", quoteField(String.format("%.2f", random.nextDouble() * 1000)));
            record.put("ADB_SUM", quoteField(String.format("%.2f", random.nextDouble() * 100000)));
            record.put("WDL_DEP_FREQ", quoteField(String.valueOf(random.nextInt(13) + 1)));
            record.put("WDL_DEP_FREQ_BAS", quoteField(String.valueOf(random.nextInt(11) + 1)));
            record.put("BAL_BFR_BREAK", quoteField(String.format("%.2f", random.nextDouble() * 50000)));
            record.put("DIWW_EDU_BRK_DT", quoteField(getRandomDate()));
            record.put("SAVE_BREAK_INT", quoteField(String.format("%.2f", random.nextDouble() * 1000)));
            record.put("SP_HOLD_VALUE", quoteField(String.format("%.2f", random.nextDouble() * 5000)));
            record.put("SRC_OF_FUND_CODE", quoteField(String.valueOf(random.nextInt(15) + 1)));
            record.put("CHQ_PASWD_TYPE", quoteField(String.valueOf(random.nextInt(9) + 1)));
            record.put("FIL01", quoteField("扩展字段" + (data.size() + 1)));

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
        return new String[] {
                "KEY_1", "TRAP", "INVM_TRM_RLLOVR_TY", "INVM_RATE_IND", "STMT_FREQUENCY", "STMT_CYCLE",
                "STOP_WDL_IND", "OMNI_FLAG", "INT_RT_CHNG_ST_IND", "SWEEP_ACCT_FLAG", "STMT_DAY",
                "NEG_RATES_FLAG", "CAS_CHQ_STOP_IND", "LIM_CNTRL_FLAG_CAP", "LST_CUST_CR_DT",
                "LST_CUST_DR_DT", "CURR_BAL", "HOLD_VAL", "INVM_TRM_VALUE", "INT_ADJUSTMENT",
                "CUSTOMER_NO", "ACCT_TYPE", "BRANCH_NO", "ACCT_OPEN_DT", "INT_FRM_DT", "INT_TO_DT",
                "INVM_MAT_DT", "CURR_STATUS", "CURRENCY", "LST_FIN_DT", "VAR_INT_RATE", "CR_STORE_RATE",
                "GL_CLASS_CODE", "INVM_TERM_BASIS", "INVM_TRM_DAY", "FIL13", "TRM_INT_ACCR",
                "ITYPE_INT_ACCR", "DIV1", "DIV2", "DIV3", "DIV4", "DIV5", "NO_OF_HOLDS", "NO_OF_NPBS",
                "NO_OF_STOPS", "PB_STATUS", "CHOP_STATUS", "FUND_TYPE", "FCY_ITEM", "FCY_CLASS",
                "DIWW_EDU_BRK_FLG", "INSTRU_CNT", "AGMT_FLAG", "PRIM_ACCT_FLG", "EMBOSS_FLAG",
                "CARD_PRIM_ACCT_IND", "PB_NUMBER", "FCY_RANGE", "NTC_LST_NTC_CNT",
                "NEGOED_INT_RATE_EXEC_MODE", "NEGOED_EXEC_INT_RATE", "NEGOED_INT_RATE_FLOAT",
                "SWINDLE_STAT", "FIL22", "SKLI_POID", "OD_VISA_AREA_1", "DR_INT_ADJUSTMENT",
                "ACCT_TYPE_CHG_FLAG", "MCURR_ACCT_TYPE"
        };
    }

    // 获取拓展表CSV头
    private static String[] getExtTableHeaders() {
        return new String[] {
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
            StringBuilder headerLine = new StringBuilder();
            for (int i = 0; i < headers.length; i++) {
                headerLine.append(quoteField(headers[i]));
                if (i < headers.length - 1) {
                    headerLine.append(DELIMITER);
                }
            }
            writer.write(headerLine.toString() + "\n");

            // 写入数据
            for (Map<String, String> record : data) {
                StringBuilder line = new StringBuilder();
                for (int i = 0; i < headers.length; i++) {
                    String value = record.getOrDefault(headers[i], quoteField(""));
                    line.append(value);
                    if (i < headers.length - 1) {
                        line.append(DELIMITER);
                    }
                }
                writer.write(line.toString() + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 为字段添加引号包围
    private static String quoteField(String field) {
        return QUOTE + field + QUOTE;
    }
}