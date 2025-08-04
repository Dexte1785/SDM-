//修改部分行多个换行符

import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class CsvDataGenerator_V3 {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
    private static final Random random = new Random();
    private static final String QUOTE = "\"";
    private static final String COMMA = "\u001B";

    // 存储已生成的KEY_1和BRANCH_NO组合，确保唯一性
    private static final Set<String> generatedKeys = new HashSet<>();
    private static final List<String> data1Records = new ArrayList<>();
    private static final List<String> data2Records = new ArrayList<>();
    private static final List<String> data3Records = new ArrayList<>();

    public static void main(String[] args) {
        String data1FilePath = "data1.csv";
        String data2FilePath = "data2.csv";
        String data3FilePath = "data3.csv";

        // 生成data1.csv - 1000条数据
        generateData1(100000, data1FilePath);

        // 生成data2.csv - 包含data1的所有数据并修改部分数据，再新增100条
        generateData2(data2FilePath);

        // 生成data3.csv - 包含data2的所有数据并修改部分数据，再新增100条
        generateData3(data3FilePath);

        System.out.println("所有CSV文件生成完成");
    }

    private static void generateData1(int numberOfRecords, String filePath) {
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write(getCsvHeader());

            for (int i = 0; i < numberOfRecords; i++) {
                String record = generateUniqueCsvRecord();
                writer.write(record);
                data1Records.add(record);
            }

            System.out.println("data1.csv生成完成: " + filePath);
        } catch (IOException e) {
            System.err.println("生成data1.csv时出错: " + e.getMessage());
        }
    }

    private static void generateData2(String filePath) {
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write(getCsvHeader());

            // 1. 包含data1的所有数据，并修改其中8%-15%的数据
            for (String record : data1Records) {
                if (random.nextDouble() < (0.08 + random.nextDouble() * 0.07)) { // 8%-15%的概率
                    String modifiedRecord = modifyRecord(record);
                    writer.write(modifiedRecord);
                    data2Records.add(modifiedRecord);
                } else {
                    writer.write(record);
                    data2Records.add(record);
                }
            }

            // 2. 新增100条数据
            for (int i = 0; i < 10000; i++) {
                String record = generateUniqueCsvRecord();
                writer.write(record);
                data2Records.add(record);
            }

            System.out.println("data2.csv生成完成: " + filePath);
        } catch (IOException e) {
            System.err.println("生成data2.csv时出错: " + e.getMessage());
        }
    }

    private static void generateData3(String filePath) {
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write(getCsvHeader());

            // 1. 包含data2的所有数据，并修改其中8%-15%的数据
            for (String record : data2Records) {
                if (random.nextDouble() < (0.08 + random.nextDouble() * 0.07)) { // 8%-15%的概率
                    String modifiedRecord = modifyRecord(record);
                    writer.write(modifiedRecord);
                    data3Records.add(modifiedRecord);
                } else {
                    writer.write(record);
                    data3Records.add(record);
                }
            }

            // 2. 新增100条数据
            for (int i = 0; i < 10000; i++) {
                String record = generateUniqueCsvRecord();
                writer.write(record);
                data3Records.add(record);
            }

            System.out.println("data3.csv生成完成: " + filePath);
        } catch (IOException e) {
            System.err.println("生成data3.csv时出错: " + e.getMessage());
        }
    }

    private static String generateUniqueCsvRecord() {
        while (true) {
            String record = generateCsvRecord();
            String key = extractKey(record);
            if (!generatedKeys.contains(key)) {
                generatedKeys.add(key);
                return record;
            }
        }
    }

    private static String extractKey(String record) {
        String[] fields = record.split(COMMA);
        // KEY_1是第一个字段，BRANCH_NO是第23个字段(索引22)
        return fields[0] + "_" + fields[22];
    }

    private static String modifyRecord(String originalRecord) {
        // 移除原始记录末尾的换行符
        String cleanedRecord = originalRecord.trim();

        String[] fields = cleanedRecord.split(COMMA, -1);  // 使用-1保留空字段

        // 修改指定字段（根据图片中的字段位置调整索引）
        fields[16] = quoteField(String.format("%.2f", random.nextDouble() * 1000000)); // CURR_BAL
        fields[18] = quoteField(String.format("%.2f", random.nextDouble() * 1000000)); // INVM_TRM_VALUE
        fields[65] = quoteField(String.format("%.2f", 1000 + random.nextDouble() * 99000)); // NRDA_THRSHLD_AMT

        // 重建记录时确保只添加一个换行符
        return String.join(COMMA, fields) + "\n";  // 只在此处添加唯一换行符
    }

    private static String getCsvHeader() {
        return quoteField("KEY_1") + COMMA + quoteField("TRAP") + COMMA + quoteField("INVM_TRM_RLLOVR_TY") + COMMA +
                quoteField("INVM_RATE_IND") + COMMA + quoteField("STMT_FREQUENCY") + COMMA + quoteField("STMT_CYCLE") + COMMA +
                quoteField("STOP_WDL_IND") + COMMA + quoteField("OMNI_FLAG") + COMMA + quoteField("INT_RT_CHNG_ST_IND") + COMMA +
                quoteField("SWEEP_ACCT_FLAG") + COMMA + quoteField("STMT_DAY") + COMMA + quoteField("NEG_RATES_FLAG") + COMMA +
                quoteField("CAS_CHQ_STOP_IND") + COMMA + quoteField("LIM_CNTRL_FLAG_CAP") + COMMA + quoteField("LST_CUST_CR_DT") + COMMA +
                quoteField("LST_CUST_DR_DT") + COMMA + quoteField("CURR_BAL") + COMMA + quoteField("HOLD_VAL") + COMMA +
                quoteField("INVM_TRM_VALUE") + COMMA + quoteField("INT_ADJUSTMENT") + COMMA + quoteField("CUSTOMER_NO") + COMMA +
                quoteField("ACCT_TYPE") + COMMA + quoteField("BRANCH_NO") + COMMA + quoteField("ACCT_OPEN_DT") + COMMA +
                quoteField("INT_FRM_DT") + COMMA + quoteField("INT_TO_DT") + COMMA + quoteField("INVM_MAT_DT") + COMMA +
                quoteField("CURR_STATUS") + COMMA + quoteField("CURRENCY") + COMMA + quoteField("LST_FIN_DT") + COMMA +
                quoteField("VAR_INT_RATE") + COMMA + quoteField("CR_STORE_RATE") + COMMA + quoteField("GL_CLASS_CODE") + COMMA +
                quoteField("INVM_TERM_BASIS") + COMMA + quoteField("INVM_TRM_DAY") + COMMA + quoteField("FIL13") + COMMA +
                quoteField("TRM_INT_ACCR") + COMMA + quoteField("ITYPE_INT_ACCR") + COMMA + quoteField("DIV1") + COMMA +
                quoteField("DIV2") + COMMA + quoteField("DIV3") + COMMA + quoteField("DIV4") + COMMA + quoteField("DIV5") + COMMA +
                quoteField("NO_OF_HOLDS") + COMMA + quoteField("NO_OF_NPBS") + COMMA + quoteField("NO_OF_STOPS") + COMMA +
                quoteField("PB_STATUS") + COMMA + quoteField("CHOP_STATUS") + COMMA + quoteField("FUND_TYPE") + COMMA +
                quoteField("FCY_ITEM") + COMMA + quoteField("FCY_CLASS") + COMMA + quoteField("DIWW_EDU_BRK_FLG") + COMMA +
                quoteField("INSTRU_CNT") + COMMA + quoteField("AGMT_FLAG") + COMMA + quoteField("PRIM_ACCT_FLG") + COMMA +
                quoteField("EMBOSS_FLAG") + COMMA + quoteField("CARD_PRIM_ACCT_IND") + COMMA + quoteField("PB_NUMBER") + COMMA +
                quoteField("FCY_RANGE") + COMMA + quoteField("NTC_LST_NTC_CNT") + COMMA + quoteField("NEGOED_INT_RATE_EXEC_MODE") + COMMA +
                quoteField("NEGOED_EXEC_INT_RATE") + COMMA + quoteField("NEGOED_INT_RATE_FLOAT") + COMMA + quoteField("SWINDLE_STAT") + COMMA +
                quoteField("FIL22") + COMMA + quoteField("SKLI_POID") + COMMA + quoteField("OD_VISA_AREA_1") + COMMA +
                quoteField("DR_INT_ADJUSTMENT") + COMMA + quoteField("ACCT_TYPE_CHG_FLAG") + COMMA + quoteField("MCURR_ACCT_TYPE") + COMMA +
                quoteField("TD_INST_AMT") + COMMA + quoteField("TEMP_ACCT_EXP_DT") + COMMA + quoteField("ANNL_REVW_DATE") + COMMA +
                quoteField("FREEZE_EXP_DATE") + COMMA + quoteField("CCDA_VSTRO_LOW_RT") + COMMA + quoteField("NRDA_THRSHLD_AMT") + COMMA +
                quoteField("FIRST_DEP_IBD_DT") + COMMA + quoteField("NTC_CNCL_INT_PEN") + COMMA + quoteField("ADB_SUM") + COMMA +
                quoteField("MAT_INT_AVAIL") + COMMA + quoteField("WDL_DEP_FREQ") + COMMA + quoteField("WDL_DEP_FREQ_BAS") + COMMA +
                quoteField("BAL_BFR_BREAK") + COMMA + quoteField("DIWW_EDU_BRK_DT") + COMMA + quoteField("SAVE_BREAK_INT") + COMMA +
                quoteField("SP_HOLD_VALUE") + COMMA + quoteField("SRC_OF_FUND_CODE") + COMMA + quoteField("CHQ_PASWD_TYPE") + COMMA +
                quoteField("FIL01") + "\n";
    }

    private static String generateCsvRecord() {
        StringBuilder record = new StringBuilder();

        // KEY_1
        record.append(quoteField(random.nextInt(900) + 100 + "" + random.nextLong(10000000000000L))).append(COMMA);

        // TRAP (1-12)
        record.append(quoteField(random.nextInt(12) + 1)).append(COMMA);

        // INVM_TRM_RLLOVR_TY (0-4,9)
        record.append(quoteField(random.nextInt(6) == 5 ? 9 : random.nextInt(5))).append(COMMA);

        // INVM_RATE_IND (0-6,9)
        record.append(quoteField(random.nextInt(8) == 7 ? 9 : random.nextInt(7))).append(COMMA);

        // STMT_FREQUENCY (1-9)
        record.append(quoteField(random.nextInt(9) + 1)).append(COMMA);

        // STMT_CYCLE (1-11,99)
        record.append(quoteField(random.nextInt(13) == 12 ? 99 : random.nextInt(11) + 1)).append(COMMA);

        // STOP_WDL_IND (0-9)
        record.append(quoteField(random.nextInt(10))).append(COMMA);

        // OMNI_FLAG (0-6,9)
        record.append(quoteField(random.nextInt(8) == 7 ? 9 : random.nextInt(7))).append(COMMA);

        // INT_RT_CHNG_ST_IND (0-9)
        record.append(quoteField(random.nextInt(10))).append(COMMA);

        // SWEEP_ACCT_FLAG (0-1)
        record.append(quoteField(random.nextInt(2))).append(COMMA);

        // STMT_DAY (1-10,99)
        record.append(quoteField(random.nextInt(12) == 11 ? 99 : random.nextInt(10) + 1)).append(COMMA);

        // NEG_RATES_FLAG (1-9)
        record.append(quoteField(random.nextInt(9) + 1)).append(COMMA);

        // CAS_CHQ_STOP_IND (0-9)
        record.append(quoteField(random.nextInt(10))).append(COMMA);

        // LIM_CNTRL_FLAG_CAP (M,S,A,U,C,V,空格)
        char[] limFlags = {'M', 'S', 'A', 'U', 'C', 'V', ' '};
        record.append(quoteField(limFlags[random.nextInt(limFlags.length)])).append(COMMA);

        // LST_CUST_CR_DT (日期)
        record.append(quoteField(generateRandomDate())).append(COMMA);

        // LST_CUST_DR_DT (日期)
        record.append(quoteField(generateRandomDate())).append(COMMA);

        // CURR_BAL (0-1000000)
        record.append(quoteField(String.format("%.2f", random.nextDouble() * 1000000))).append(COMMA);

        // HOLD_VAL (0-100000)
        record.append(quoteField(String.format("%.2f", random.nextDouble() * 100000))).append(COMMA);

        // INVM_TRM_VALUE (0-1000000)
        record.append(quoteField(String.format("%.2f", random.nextDouble() * 1000000))).append(COMMA);

        // INT_ADJUSTMENT (-1000-1000)
        record.append(quoteField(String.format("%.2f", (random.nextDouble() * 2000) - 1000))).append(COMMA);

        // CUSTOMER_NO (P,C,G,F,I,O,S,B,T,E + 数字)
        char[] customerTypes = {'P', 'C', 'G', 'F', 'I', 'O', 'S', 'B', 'T', 'E'};
        record.append(quoteField(customerTypes[random.nextInt(customerTypes.length)] + "" +
                (random.nextInt(90000000) + 10000000))).append(COMMA);

        // ACCT_TYPE (1-15,99)
        record.append(quoteField(random.nextInt(17) == 16 ? 99 : random.nextInt(15) + 1)).append(COMMA);

        // BRANCH_NO (100-999)
        record.append(quoteField(random.nextInt(900) + 100)).append(COMMA);

        // ACCT_OPEN_DT (日期)
        record.append(quoteField(generateRandomDate())).append(COMMA);

        // INT_FRM_DT (日期)
        record.append(quoteField(generateRandomDate())).append(COMMA);

        // INT_TO_DT (日期)
        record.append(quoteField(generateRandomDate())).append(COMMA);

        // INVM_MAT_DT (日期)
        record.append(quoteField(generateRandomDate())).append(COMMA);

        // CURR_STATUS (0-12,99)
        record.append(quoteField(random.nextInt(14) == 13 ? 99 : random.nextInt(13))).append(COMMA);

        // CURRENCY (001-020,099)
        record.append(quoteField(String.format("%03d", random.nextInt(21) == 20 ? 99 : random.nextInt(20) + 1))).append(COMMA);

        // LST_FIN_DT (日期)
        record.append(quoteField(generateRandomDate())).append(COMMA);

        // VAR_INT_RATE (0.1-10)
        record.append(quoteField(String.format("%.4f", 0.1 + random.nextDouble() * 9.9))).append(COMMA);

        // CR_STORE_RATE (0.1-10)
        record.append(quoteField(String.format("%.4f", 0.1 + random.nextDouble() * 9.9))).append(COMMA);

        // GL_CLASS_CODE (特定值)
        String[] glClassCodes = {"1000","1100","1200","1300","1400","2000","2100","2200",
                "2300","2400","3000","4000","5000","5100","5200","5300","5400","9000"};
        record.append(quoteField(glClassCodes[random.nextInt(glClassCodes.length)])).append(COMMA);

        // INVM_TERM_BASIS (1-9)
        record.append(quoteField(random.nextInt(9) + 1)).append(COMMA);

        // INVM_TRM_DAY (1-3650)
        record.append(quoteField(random.nextInt(3650) + 1)).append(COMMA);

        // FIL13 (备注)
        record.append(quoteField("")).append(COMMA);

        // TRM_INT_ACCR (0-10000)
        record.append(quoteField(String.format("%.2f", random.nextDouble() * 10000))).append(COMMA);

        // ITYPE_INT_ACCR (-1000-1000)
        record.append(quoteField(String.format("%.2f", (random.nextDouble() * 2000) - 1000))).append(COMMA);

        // DIV1-DIV5 (0-1000, 8位小数)
        for (int i = 0; i < 5; i++) {
            record.append(quoteField(String.format("%.8f", random.nextDouble() * 1000))).append(COMMA);
        }

        // NO_OF_HOLDS (0-100)
        record.append(quoteField(random.nextInt(101))).append(COMMA);

        // NO_OF_NPBS (0-100)
        record.append(quoteField(random.nextInt(101))).append(COMMA);

        // NO_OF_STOPS (0-100)
        record.append(quoteField(random.nextInt(101))).append(COMMA);

        // PB_STATUS (0-9)
        record.append(quoteField(random.nextInt(10))).append(COMMA);

        // CHOP_STATUS (0-9)
        record.append(quoteField(random.nextInt(10))).append(COMMA);

        // FUND_TYPE (1-12,99)
        record.append(quoteField(random.nextInt(13) == 12 ? 99 : random.nextInt(12) + 1)).append(COMMA);

        // FCY_ITEM (1-17,99)
        record.append(quoteField(random.nextInt(18) == 17 ? 99 : random.nextInt(17) + 1)).append(COMMA);

        // FCY_CLASS (1-15,99)
        record.append(quoteField(random.nextInt(16) == 15 ? 99 : random.nextInt(15) + 1)).append(COMMA);

        // DIWW_EDU_BRK_FLG (1-15,99)
        record.append(quoteField(random.nextInt(16) == 15 ? 99 : random.nextInt(15) + 1)).append(COMMA);

        // INSTRU_CNT (1-10,99)
        record.append(quoteField(random.nextInt(11) == 10 ? 99 : random.nextInt(10) + 1)).append(COMMA);

        // AGMT_FLAG (0-9)
        record.append(quoteField(random.nextInt(10))).append(COMMA);

        // PRIM_ACCT_FLG (0-6,9)
        record.append(quoteField(random.nextInt(8) == 7 ? 9 : random.nextInt(7))).append(COMMA);

        // EMBOSS_FLAG (0-3,9)
        record.append(quoteField(random.nextInt(5) == 4 ? 9 : random.nextInt(4))).append(COMMA);

        // CARD_PRIM_ACCT_IND (0-6,9)
        record.append(quoteField(random.nextInt(8) == 7 ? 9 : random.nextInt(7))).append(COMMA);

        // PB_NUMBER (PB + 数字)
        record.append(quoteField("PB" + (random.nextInt(900000000) + 100000000))).append(COMMA);

        // FCY_RANGE (1-15,99)
        record.append(quoteField(random.nextInt(16) == 15 ? 99 : random.nextInt(15) + 1)).append(COMMA);

        // NTC_LST_NTC_CNT (1-15,99)
        record.append(quoteField(random.nextInt(16) == 15 ? 99 : random.nextInt(15) + 1)).append(COMMA);

        // NEGOED_INT_RATE_EXEC_MODE (1-10,99)
        record.append(quoteField(random.nextInt(11) == 10 ? 99 : random.nextInt(10) + 1)).append(COMMA);

        // NEGOED_EXEC_INT_RATE (0.1-10)
        record.append(quoteField(String.format("%.8f", 0.1 + random.nextDouble() * 9.9))).append(COMMA);

        // NEGOED_INT_RATE_FLOAT (-5-5)
        record.append(quoteField(String.format("%.8f", (random.nextDouble() * 10) - 5))).append(COMMA);

        // SWINDLE_STAT (0-9)
        record.append(quoteField(random.nextInt(10))).append(COMMA);

        // FIL22 (1-15,99)
        record.append(quoteField(random.nextInt(16) == 15 ? 99 : random.nextInt(15) + 1)).append(COMMA);

        // SKLI_POID (100-130,999)
        record.append(quoteField(random.nextInt(31) == 30 ? 999 : random.nextInt(30) + 100)).append(COMMA);

        // OD_VISA_AREA_1 (1-9)
        record.append(quoteField(random.nextInt(9) + 1)).append(COMMA);

        // DR_INT_ADJUSTMENT (-1000-1000)
        record.append(quoteField(String.format("%.5f", (random.nextDouble() * 2000) - 1000))).append(COMMA);

        // ACCT_TYPE_CHG_FLAG (1-2)
        record.append(quoteField(random.nextInt(2) + 1)).append(COMMA);

        // MCURR_ACCT_TYPE (0-11,99)
        record.append(quoteField(random.nextInt(13) == 12 ? 99 : random.nextInt(12))).append(COMMA);

        // TD_INST_AMT (0-100000)
        record.append(quoteField(String.format("%.2f", random.nextDouble() * 100000))).append(COMMA);

        // TEMP_ACCT_EXP_DT (未来日期)
        record.append(quoteField(generateFutureDate())).append(COMMA);

        // ANNL_REVW_DATE (未来日期)
        record.append(quoteField(generateFutureDate())).append(COMMA);

        // FREEZE_EXP_DATE (未来日期)
        record.append(quoteField(generateFutureDate())).append(COMMA);

        // CCDA_VSTRO_LOW_RT (0.1-10)
        record.append(quoteField(String.format("%.4f", 0.1 + random.nextDouble() * 9.9))).append(COMMA);

        // NRDA_THRSHLD_AMT (1000-100000)
        record.append(quoteField(String.format("%.2f", 1000 + random.nextDouble() * 99000))).append(COMMA);

        // FIRST_DEP_IBD_DT (日期)
        record.append(quoteField(generateRandomDate())).append(COMMA);

        // NTC_CNCL_INT_PEN (0-1000)
        record.append(quoteField(String.format("%.2f", random.nextDouble() * 1000))).append(COMMA);

        // ADB_SUM (0-1000000)
        record.append(quoteField(String.format("%.2f", random.nextDouble() * 1000000))).append(COMMA);

        // MAT_INT_AVAIL (0-10000)
        record.append(quoteField(String.format("%.2f", random.nextDouble() * 10000))).append(COMMA);

        // WDL_DEP_FREQ (1-12,99)
        record.append(quoteField(random.nextInt(13) == 12 ? 99 : random.nextInt(12) + 1)).append(COMMA);

        // WDL_DEP_FREQ_BAS (1-10,99)
        record.append(quoteField(random.nextInt(11) == 10 ? 99 : random.nextInt(10) + 1)).append(COMMA);

        // BAL_BFR_BREAK (0-1000000)
        record.append(quoteField(String.format("%.2f", random.nextDouble() * 1000000))).append(COMMA);

        // DIWW_EDU_BRK_DT (日期)
        record.append(quoteField(generateRandomDate())).append(COMMA);

        // SAVE_BREAK_INT (0-1000)
        record.append(quoteField(String.format("%.2f", random.nextDouble() * 1000))).append(COMMA);

        // SP_HOLD_VALUE (0-100000)
        record.append(quoteField(String.format("%.2f", random.nextDouble() * 100000))).append(COMMA);

        // SRC_OF_FUND_CODE (1-15,99)
        record.append(quoteField(random.nextInt(16) == 15 ? 99 : random.nextInt(15) + 1)).append(COMMA);

        // CHQ_PASWD_TYPE (1-9)
        record.append(quoteField(random.nextInt(9) + 1)).append(COMMA);

        // FIL01 (扩展字段)
        record.append(quoteField(""));

        record.append("\n");
        return record.toString();
    }

    private static String generateRandomDate() {
        // 生成2000-01-01到2023-12-31之间的随机日期
        long start = new GregorianCalendar(2000, 0, 1).getTimeInMillis();
        long end = new GregorianCalendar(2023, 11, 31).getTimeInMillis();
        long randomDate = ThreadLocalRandom.current().nextLong(start, end + 1);
        return dateFormat.format(new Date(randomDate));
    }

    private static String generateFutureDate() {
        // 生成未来365天内的日期
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, random.nextInt(365) + 1);
        return dateFormat.format(cal.getTime());
    }

    private static String quoteField(Object field) {
        return QUOTE + field.toString() + QUOTE;
    }
}