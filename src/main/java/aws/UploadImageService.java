package aws;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

import java.io.File;

public class UploadImageService {

  private static final String AWS_ACCESS_KEY = "AKIASKU3UW7RS3BUCE5Q";
  private static final String AWS_SECRET_KEY = "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";

  public void uploadFile(String filename) throws Exception {
    String bucketName = "mascotas-bucket";

    try {
      BasicAWSCredentials credentials = new BasicAWSCredentials(AWS_ACCESS_KEY, AWS_SECRET_KEY);
      AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
          .withRegion(Regions.SA_EAST_1)
          .withCredentials(new AWSStaticCredentialsProvider(credentials))
          .build();

      // Upload a file as a new object with ContentType and title specified.
      PutObjectRequest request = new PutObjectRequest(bucketName, filename, new File("./fotos-mascotas/" + filename));
      ObjectMetadata metadata = new ObjectMetadata();
      metadata.setContentType("image/png");
      request.setMetadata(metadata);
      s3Client.putObject(request);
    } catch (Exception e) {
      // The call was transmitted successfully, but Amazon S3 couldn't process
      // it, so it returned an error response.
      e.printStackTrace();
      throw new Exception();
    }
  }

  public void uploadFileEncontrada(String filename) throws Exception {
    String bucketName = "mascotas-bucket";

    try {
      BasicAWSCredentials credentials = new BasicAWSCredentials(AWS_ACCESS_KEY, AWS_SECRET_KEY);
      AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
          .withRegion(Regions.SA_EAST_1)
          .withCredentials(new AWSStaticCredentialsProvider(credentials))
          .build();

      // Upload a file as a new object with ContentType and title specified.
      PutObjectRequest request = new PutObjectRequest(bucketName, filename, new File("./fotos-mascotas-encontradas/" + filename));
      ObjectMetadata metadata = new ObjectMetadata();
      metadata.setContentType("image/png");
      request.setMetadata(metadata);
      s3Client.putObject(request);
    } catch (Exception e) {
      // The call was transmitted successfully, but Amazon S3 couldn't process
      // it, so it returned an error response.
      e.printStackTrace();
      throw new Exception();
    }
  }

}