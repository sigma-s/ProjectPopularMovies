# ProjectPopularMovies
This android app will:

    Upon launch, present the user with an grid arrangement of movie posters.
    Allow your user to change sort order via a setting:

    The sort order can be by most popular, or by highest-rated

    Allow the user to tap on a movie poster and transition to a details screen with additional information such as:

    original title
    movie poster image thumbnail
    A plot synopsis (called overview in the api)
    user rating (called top_rated in the api)
    release date
    
  In order to run the code the user has to go to theMovieDBAPI and get an API key from there. The API key has to be
  substituted in the MainActivity.java where there is a text "enter your API key here". Without it, the code will
  not run.
  
  Here are the instructions for getting the API key:
  
  To fetch popular movies, you will use the API from themoviedb.org.

  If you don’t already have an account, you will need to create one in order to request an API Key. 

  In your request for a key, state that your usage will be for educational/non-commercial use. 
  You will also need to provide some personal information to complete the request. Once you 
  submit your request, you should receive your key via email shortly after.

  In order to request popular movies you will want to request data from the /discover/movie endpoint. An API Key is required.
  Once you obtain your key, you append it to your HTTP request as a URL parameter like so:

  http://api.themoviedb.org/3/movie/popular?api_key=[YOUR_API_KEY]

  You will extract the movie id from this request. You will need this in subsequent requests.
 
