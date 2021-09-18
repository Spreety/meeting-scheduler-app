package Model;

/** This class is where the constructors, and getters are kept */
public class Reports {

    private String reportName;

    //constructor
    public Reports(String reportName) {
        this.reportName = reportName;
    }

    //getter
    public String getReportName() {
        return reportName;
    }

    //this overrides the toString in relation to the combo boxes
    @Override
    public String toString() {
        return reportName;
    }
}
