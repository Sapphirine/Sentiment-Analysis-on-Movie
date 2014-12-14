Sentiment Analysis on Movie Reviews
===============================

Introduction
----
Movie review sites allows users to submit comments to describe their favorite or not about a particular movie. These comments can be used to dig and generate useful data that describe its contents, providing an opportunity to learn about the sentiment of that review. With the growth of the internet, we have more source of hearing what other people say about a movie. However most times these reviews are pure texts, and we have to read them through to know the sentiment behind the reviews. Using machine learning, we can democratize anything subjectively in the world. We can analyze the subjective and objective content, allowing us to better understand the products and services, we can also use it to make better decisions. Most of previous works will classify movie reviews into two categories: good and bad. However it’s hard to represent the full meaning by just two categories. So we want the result to be with more than two results, in fact, we will classify the reviews into four results: a score from 0 to 3. In this paper, we used technologies of large-scale machine learning and natural language processing, to extract a score representing the sentiment. Then we can learn the movie is good or bad.

Demo Website
----
http://movie-review.elasticbeanstalk.com


How to Use Code
----
This project is a Maven based Java Web application. However, we also provide main method in most of the essential classes like data preprocessing, Naive Bayes training & testing, Logistical Regression training & testing.

1. Run as web application:
* Clone this repository and import it into eclipse as Maven project
* Right Click on the project name and choose Run as Maven install, then a war package will be generated under /target
* You can either deploy the war package into a tomcat based web server or just choose Run as -> Run on Server
2. Preprocessing data: 
* com.cu.bigdata.moviereview.BuildVector.Preprocessor: the input are two file directory - one for movie review and another for the rating of the movie review. The output is a csv file which represents the converted vector of each movie review.
3. Naive Bayes:
* com.cu.bigdata.moviereview.NavieBays
4. Logistical Regression:
* com.cu.bigdata.moviereview.LogisticRegression


Contributors
------
Yunao Liu yl3055@columbia.edu

Hao Tong  ht2358@columbia.edu

Di Liu  dl2870@columbia.edu

Acknowledgments
------
We would like to thank Professor CHING-YUNG LIN and all TAs for providing keen insights and keeping us on track. 