package sg.edu.np.mad.pawgress;

public class PetCompanions {
    private String type;
    private String design;

    public String petType;
    public String petDesign;

    public PetCompanions(String petType, String petDesign) {
        this.type = petType;
        this.design = petDesign;
    }

    public String getType() {
        return petType;
    }

    public String getDesign() {
        return petDesign;
    }
}
