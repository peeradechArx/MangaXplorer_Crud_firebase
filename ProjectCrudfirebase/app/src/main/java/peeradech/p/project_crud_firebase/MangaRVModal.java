package peeradech.p.project_crud_firebase;

import android.os.Parcel;
import android.os.Parcelable;

public class MangaRVModal implements Parcelable {
    //creating variables for our different fields.
    private String MangaName;
    private String MangaDescription;
    private String MangaPrice;
    private String bestSuitedFor;
    private String MangaImg;
    private String MangaLink;
    private String MangaId;


    public String getMangaId() {
        return MangaId;
    }

    public void setMangaId(String MangaId) {
        this.MangaId = MangaId;
    }


    //creating an empty constructor.
    public MangaRVModal() {

    }

    protected MangaRVModal(Parcel in) {
        MangaName = in.readString();
        MangaId = in.readString();
        MangaDescription = in.readString();
        MangaPrice = in.readString();
        bestSuitedFor = in.readString();
        MangaImg = in.readString();
        MangaLink = in.readString();
    }

    public static final Creator<MangaRVModal> CREATOR = new Creator<MangaRVModal>() {
        @Override
        public MangaRVModal createFromParcel(Parcel in) {
            return new MangaRVModal(in);
        }

        @Override
        public MangaRVModal[] newArray(int size) {
            return new MangaRVModal[size];
        }
    };

    //creating getter and setter methods.
    public String getMangaName() {
        return MangaName;
    }

    public void setMangaName(String MangaName) {
        this.MangaName = MangaName;
    }

    public String getMangaDescription() {
        return MangaDescription;
    }

    public void setMangaDescription(String MangaDescription) {
        this.MangaDescription = MangaDescription;
    }

    public String getMangaPrice() {
        return MangaPrice;
    }

    public void setMangaPrice(String MangaPrice) {
        this.MangaPrice = MangaPrice;
    }

    public String getBestSuitedFor() {
        return bestSuitedFor;
    }

    public void setBestSuitedFor(String bestSuitedFor) {
        this.bestSuitedFor = bestSuitedFor;
    }

    public String getMangaImg() {
        return MangaImg;
    }

    public void setMangaImg(String MangaImg) {
        this.MangaImg = MangaImg;
    }

    public String getMangaLink() {
        return MangaLink;
    }

    public void setMangaLink(String MangaLink) {
        this.MangaLink = MangaLink;
    }


    public MangaRVModal(String MangaId, String MangaName, String MangaDescription, String MangaPrice, String bestSuitedFor, String MangaImg, String MangaLink) {
        this.MangaName = MangaName;
        this.MangaId = MangaId;
        this.MangaDescription = MangaDescription;
        this.MangaPrice = MangaPrice;
        this.bestSuitedFor = bestSuitedFor;
        this.MangaImg = MangaImg;
        this.MangaLink = MangaLink;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(MangaName);
        dest.writeString(MangaId);
        dest.writeString(MangaDescription);
        dest.writeString(MangaPrice);
        dest.writeString(bestSuitedFor);
        dest.writeString(MangaImg);
        dest.writeString(MangaLink);
    }
}
