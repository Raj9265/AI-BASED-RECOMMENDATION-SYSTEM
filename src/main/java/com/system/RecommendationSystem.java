package com.system;

import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.recommender.GenericItemBasedRecommender;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;
import org.apache.mahout.cf.taste.similarity.ItemSimilarity;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import java.io.File;
import java.util.List;

public class RecommendationSystem {
    public static void main(String[] args) {
        try {
          
            File file = new File("src/main/resources/data.csv");
            DataModel model = new FileDataModel(file);

           
            UserSimilarity userSimilarity = new PearsonCorrelationSimilarity(model);
            UserNeighborhood neighborhood = new NearestNUserNeighborhood(2, userSimilarity, model);
            Recommender userBasedRecommender = new GenericUserBasedRecommender(model, neighborhood, userSimilarity);

           
            System.out.println("Similarity between users:");
            for (long userID1 = 1; userID1 <= 3; userID1++) {
                for (long userID2 = userID1 + 1; userID2 <= 3; userID2++) {
                    double simScore = userSimilarity.userSimilarity(userID1, userID2);
                    System.out.printf("User %d and User %d similarity: %.2f%n", userID1, userID2, simScore);
                }
            }

           
            System.out.println("\nUser-Based Recommendations:");
            List<RecommendedItem> userRecommendations = userBasedRecommender.recommend(1, 3);
            System.out.println("Recommendations generated for User 1: " + userRecommendations.size());
            for (RecommendedItem recommendation : userRecommendations) {
                System.out.printf("Recommended Item: %d, Value: %.2f%n", recommendation.getItemID(), recommendation.getValue());
            }

        
            ItemSimilarity itemSimilarity = new PearsonCorrelationSimilarity(model);
            Recommender itemBasedRecommender = new GenericItemBasedRecommender(model, itemSimilarity);

           
            System.out.println("\nItem-Based Recommendations:");
            List<RecommendedItem> itemRecommendations = itemBasedRecommender.recommend(1, 3);
            for (RecommendedItem recommendation : itemRecommendations) {
                System.out.printf("Recommended Item: %d, Value: %.2f%n", recommendation.getItemID(), recommendation.getValue());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
