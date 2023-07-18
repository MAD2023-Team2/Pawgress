package sg.edu.np.mad.pawgress.Fragments.Profile;

public class ProfilePicture {
    private int imageResId;
    private String imagePath;

    // Constructor for image resource ID
    public ProfilePicture(int imageResId) {
        this.imageResId = imageResId;
    }

    // Constructor for image file path
    public ProfilePicture(String imagePath) {
        this.imagePath = imagePath;
    }

    // Getters and Setters
    public int getImageResId() {
        return imageResId;
    }

    public String getImagePath() {
        return imagePath;
    }
}
