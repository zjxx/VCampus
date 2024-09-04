package app.vcampus.enums;
//枚举专业：1 建筑学院 2 机械工程学院3 能源与环境学院4 信息科学与工程学院5 土木工程学院6 电子科学与工程学院7 数学系8 自动化学院9 计算机科学与工程学院10 物理系11 生物科学与医学工程学院12 材料科学与工程学院13 人文学院14 经济管理学院； 16 电气工程学院17 外国语学院18 体育系19 化学化工学院21 交通学院22 仪器科学与工程学院24 艺术学院25 法学院26 学习科学研究中心41 基础医学院42 公共卫生学院43 临床医学院61 吴健雄学院71 软件学院
public enum MajorType{
    ARCHITECTURE("建筑学院"),//1
    MECHANICAL_ENGINEERING("机械工程学院"),//2
    ENERGY_ENVIRONMENT("能源与环境学院"),//3
    INFORMATION_SCIENCE_ENGINEERING("信息科学与工程学院"),//4
    CIVIL_ENGINEERING("土木工程学院"),//5
    ELECTRONIC_SCIENCE_ENGINEERING("电子科学与工程学院"),//6
    MATHEMATICS("数学系"),//7
    AUTOMATION_ENGINEERING("自动化学院"),//8
    COMPUTER_SCIENCE_ENGINEERING("计算机科学与工程学院"),//9
    PHYSICS("物理系"),//10
    BIOMEDICAL_ENGINEERING("生物科学与医学工程学院"),//11
    MATERIALS_SCIENCE_ENGINEERING("材料科学与工程学院"),//12
    HUMANITIES("人文学院"),//13
    ECONOMIC_MANAGEMENT("经济管理学院"),//14
    ELECTRICAL_ENGINEERING("电气工程学院"),//15
    FOREIGN_LANGUAGES("外国语学院"),//16
    PHYSICAL_EDUCATION("体育系"),//17
    CHEMICAL_ENGINEERING("化学化工学院"),//18
    TRANSPORTATION("交通学院"),//19
    INSTRUMENTATION_ENGINEERING("仪器科学与工程学院"),//20
    ART("艺术学院"),//21
    LAW("法学院"),//22
    LEARNING_SCIENCE_RESEARCH_CENTER("学习科学研究中心"),//23
    BASIC_MEDICINE("基础医学院"),//24
    PUBLIC_HEALTH("公共卫生学院"),//25
    CLINICAL_MEDICINE("临床医学院"),//26
    WUJIANXIONG_COLLEGE("吴健雄学院"),//27
    SOFTWARE_COLLEGE("软件学院");//28
    private final String major;

    MajorType(String major) {
        this.major = major;
    }

    public String getMajor() {
        return major;
    }

    public static String fromIndex(int index) {
        MajorType[] values = MajorType.values();
        if (index < 1 || index >= values.length+1) // 从1开始
        {
            throw new IndexOutOfBoundsException("Invalid index: " + index);
        }
        return values[index].getMajor();
    }
}
