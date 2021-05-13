package dataInfoLogic.InternalServices;

import com.google.cloud.language.v1.*;

import java.io.IOException;


public class Categorization {

    public String categorizeString(String input) throws IOException {

        String output = new String();

        String text = "Facebook (stylized as facebook) is an American online social media and social networking service based in Menlo Park, California, and a flagship service of the namesake company Facebook, Inc.";

            // Instantiate the Language client com.google.cloud.language.v1.LanguageServiceClient
            try (LanguageServiceClient language = LanguageServiceClient.create()) {
                // set content to the text string
                Document doc = Document.newBuilder().setContent(text).setType(Document.Type.PLAIN_TEXT).build();
                ClassifyTextRequest request = ClassifyTextRequest.newBuilder().setDocument(doc).build();
                // detect categories in the given text
                ClassifyTextResponse response = language.classifyText(request);
                output = response.toString();

                for (ClassificationCategory category : response.getCategoriesList()) {
                    System.out.printf(
                            "Category name : %s, Confidence : %.3f\n",
                            category.getName(), category.getConfidence());
                }
            }catch(Exception e){
                System.out.println(e);
            }


        return output;
    }
}
