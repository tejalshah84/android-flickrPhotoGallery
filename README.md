# android-flickrPhotoGallery
Summary of Changes to Implement Search:

  res/menu/fragment_photo_gallery - Specified ActionView class as SearchView
  
  PhotoGalleryFragment - Added constructors to FetchItemsTask to accept query text
                       - Modified onCreateOptionsMenu methos to add listener to SearchView
                       - Created a listener class (SearchView.OnQueryTextListener queryTextListener) that observes text changes in the search bar
