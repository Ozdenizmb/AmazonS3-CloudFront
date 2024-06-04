# AmazonS3-CloudFront

<h1>The Spring project was written to learn how to use Amazon S3 and CloudFront (CDN).</h1>


<p>Things you need to pay attention to for the Spring project to run successfully:</p>

<h3>1. Configure yaml file</h3>

<p>Update the database's username and password information to be compatible with your own database.</p>
<p>Make sure you have a database named asset_service</p>

<h3>2. Create a file named gradle.properties</h3>

<p>awsAccessKey=&lt;YOUR_AWS_ACCESS_KEY&gt;</p>
<p>awsSecretKey=&lt;YOUR_AWS_SECRET_KEY&gt;</p>
<p>awsS3BucketName=&lt;YOUR_AWS_BUCKET_NAME&gt;</p>
<p>awsRegion=&lt;YOUR_AWS_REGION_NAME&gt;</p>
<p>awsCdnPath=&lt;YOUR_AWS_CDN_PATH&gt;</p>
