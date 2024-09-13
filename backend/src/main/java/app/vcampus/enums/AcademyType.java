package app.vcampus.enums;

/**
 * Enum representing various academies.
 * Each enum constant is associated with a Chinese name of the academy.
 */
public enum AcademyType {
    ARCHITECTURE("建筑学院"), // 1
    MECHANICAL_ENGINEERING("机械工程学院"), // 2
    ENERGY_ENVIRONMENT("能源与环境学院"), // 3
    INFORMATION_SCIENCE_ENGINEERING("信息科学与工程学院"), // 4
    CIVIL_ENGINEERING("土木工程学院"), // 5
    ELECTRONIC_SCIENCE_ENGINEERING("电子科学与工程学院"), // 6
    MATHEMATICS("数学系"), // 7
    AUTOMATION_ENGINEERING("自动化学院"), // 8
    COMPUTER_SCIENCE_ENGINEERING("计算机科学与工程学院"), // 9
    PHYSICS("物理系"), // 10
    BIOMEDICAL_ENGINEERING("生物科学与医学工程学院"), // 11
    MATERIALS_SCIENCE_ENGINEERING("材料科学与工程学院"), // 12
    HUMANITIES("人文学院"), // 13
    ECONOMIC_MANAGEMENT("经济管理学院"), // 14
    ELECTRICAL_ENGINEERING("电气工程学院"), // 15
    FOREIGN_LANGUAGES("外国语学院"), // 16
    PHYSICAL_EDUCATION("体育系"), // 17
    CHEMICAL_ENGINEERING("化学化工学院"), // 18
    TRANSPORTATION("交通学院"), // 19
    INSTRUMENTATION_ENGINEERING("仪器科学与工程学院"), // 20
    ART("艺术学院"), // 21
    LAW("法学院"), // 22
    LEARNING_SCIENCE_RESEARCH_CENTER("学习科学研究中心"), // 23
    BASIC_MEDICINE("基础医学院"), // 24
    PUBLIC_HEALTH("公共卫生学院"), // 25
    CLINICAL_MEDICINE("临床医学院"), // 26
    WUJIANXIONG_COLLEGE("吴健雄学院"), // 27
    SOFTWARE_COLLEGE("软件学院"); // 28

    private final String major;

    /**
     * Constructor for AcademyType enum.
     *
     * @param major the Chinese name of the academy
     */
    AcademyType(String major) {
        this.major = major;
    }

    /**
     * Gets the Chinese name of the academy.
     *
     * @return the Chinese name of the academy
     */
    public String getMajor() {
        return major;
    }

    /**
     * Gets the Chinese name of the academy based on the index.
     *
     * @param index the index of the academy (starting from 1)
     * @return the Chinese name of the academy
     * @throws IndexOutOfBoundsException if the index is out of range
     */
    public static String fromIndex(int index) {
        AcademyType[] values = AcademyType.values();
        if (index < 1 || index >= values.length + 1) { // 从1开始
            throw new IndexOutOfBoundsException("Invalid index: " + index);
        }
        return values[index].getMajor();
    }

    /**
     * Gets the index of the academy based on the Chinese name.
     *
     * @param major the Chinese name of the academy
     * @return the index of the academy (starting from 1)
     * @throws IndexOutOfBoundsException if the major is not found
     */
    public static int getIndex(String major) {
        AcademyType[] values = AcademyType.values();
        for (int i = 0; i < values.length; i++) {
            if (values[i].getMajor().equals(major)) {
                return i + 1;
            }
        }
        throw new IndexOutOfBoundsException("Invalid major: " + major);
    }
}