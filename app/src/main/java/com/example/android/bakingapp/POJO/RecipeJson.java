package com.example.android.bakingapp.POJO;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

@Entity(tableName = "Recipe")
public class RecipeJson implements Parcelable {

    @PrimaryKey
    private int primId = 0;
    private int pos;
    @SerializedName("id")
    private int id;
    @SerializedName("name")
    private String name;
    @SerializedName("image")
    private String imageUrl;
    @SerializedName("ingredients")
    private List<Ingredients> ingredients;
    @SerializedName("steps")
    private List<Steps> steps;
    @SerializedName("servings")
    private int servings;

    public RecipeJson(int primId, int pos, int id, String name, String imageUrl, List<Ingredients> ingredients, List<Steps> steps, int servings) {
        this.primId = primId;
        this.pos = pos;
        this.id = id;
        this.name = name;
        this.imageUrl = imageUrl;
        this.ingredients = ingredients;
        this.steps = steps;
        this.servings = servings;
    }

    protected RecipeJson(Parcel in) {
        primId = in.readInt();
        pos = in.readInt();
        id = in.readInt();
        name = in.readString();
        imageUrl = in.readString();
        ingredients = in.createTypedArrayList(Ingredients.CREATOR);
        steps = in.createTypedArrayList(Steps.CREATOR);
        servings = in.readInt();
    }

    public static final Creator<RecipeJson> CREATOR = new Creator<RecipeJson>() {
        @Override
        public RecipeJson createFromParcel(Parcel in) {
            return new RecipeJson(in);
        }

        @Override
        public RecipeJson[] newArray(int size) {
            return new RecipeJson[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }


    public List<Ingredients> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredients> ingredients) {
        this.ingredients = ingredients;
    }

    public List<Steps> getSteps() {
        return steps;
    }


    public void setSteps(List<Steps> steps) {
        this.steps = steps;
    }

    public int getServings() {
        return servings;
    }

    public void setServings(int servings) {
        this.servings = servings;
    }

    public int getPrimId() {
        return primId;
    }

    public void setPrimId(int primId) {
        this.primId = primId;
    }

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(primId);
        parcel.writeInt(pos);
        parcel.writeInt(id);
        parcel.writeString(name);
        parcel.writeString(imageUrl);
        parcel.writeTypedList(ingredients);
        parcel.writeTypedList(steps);
        parcel.writeInt(servings);
    }

    public static class Ingredients implements Parcelable {
        @SerializedName("quantity")
        private double quantity;
        @SerializedName("measure")
        private String measure;
        @SerializedName("ingredient")
        private String ingredient;

        public Ingredients(double quantity, String measure, String ingredient) {
            this.quantity = quantity;
            this.measure = measure;
            this.ingredient = ingredient;
        }

        protected Ingredients(Parcel in) {
            quantity = in.readDouble();
            measure = in.readString();
            ingredient = in.readString();
        }

        public static final Creator<Ingredients> CREATOR = new Creator<Ingredients>() {
            @Override
            public Ingredients createFromParcel(Parcel in) {
                return new Ingredients(in);
            }

            @Override
            public Ingredients[] newArray(int size) {
                return new Ingredients[size];
            }
        };

        public double getQuantity() {
            return quantity;
        }

        public void setQuantity(double quantity) {
            this.quantity = quantity;
        }

        public String getMeasure() {
            return measure;
        }

        public void setMeasure(String measure) {
            this.measure = measure;
        }

        public String getIngredient() {
            return ingredient;
        }

        public void setIngredient(String ingredient) {
            this.ingredient = ingredient;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeDouble(quantity);
            parcel.writeString(measure);
            parcel.writeString(ingredient);
        }
    }

    public static class Steps implements Parcelable {
        @SerializedName("id")
        private int id;
        @SerializedName("shortDescription")
        private String shortDescription;
        @SerializedName("description")
        private String description;
        @SerializedName("videoURL")
        private String videoUrl;
        @SerializedName("thumbnailUrl")
        private String thumbnailUrl;


        public Steps(int id, String shortDescription, String description, String videoUrl, String thumbnailUrl) {
            this.id = id;
            this.shortDescription = shortDescription;
            this.description = description;
            this.videoUrl = videoUrl;
            this.thumbnailUrl = thumbnailUrl;
        }

        protected Steps(Parcel in) {
            id = in.readInt();
            shortDescription = in.readString();
            description = in.readString();
            videoUrl = in.readString();
            thumbnailUrl = in.readString();
        }

        public static final Creator<Steps> CREATOR = new Creator<Steps>() {
            @Override
            public Steps createFromParcel(Parcel in) {
                return new Steps(in);
            }

            @Override
            public Steps[] newArray(int size) {
                return new Steps[size];
            }
        };

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getShortDescription() {
            return shortDescription;
        }

        public void setShortDescription(String shortDescription) {
            this.shortDescription = shortDescription;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getVideoUrl() {
            return videoUrl;
        }

        public void setVideoUrl(String videoUrl) {
            this.videoUrl = videoUrl;
        }

        public String getThumbnailUrl() {
            return thumbnailUrl;
        }

        public void setThumbnailUrl(String thumbnailUrl) {
            this.thumbnailUrl = thumbnailUrl;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeInt(id);
            parcel.writeString(shortDescription);
            parcel.writeString(description);
            parcel.writeString(videoUrl);
            parcel.writeString(thumbnailUrl);
        }
    }
}
