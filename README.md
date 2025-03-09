# GlobalWaves #


**Project description**

The project consists of an implementation of an audio browsing app, like
Spotify, which has command support for different types of users. The normal
users have a generic audio player with basic functionalities and paging
system. They can also access pages of artists and hosts. The app has
monetization, statistics and recommendation features for each user. 

**App features**

Admin commands:
- addUser, deleteUser
- showAlbums, showPlaylists

User commands:
- switchConnectionStatus
- subscribe, getNotifications
- updateRecommendations, loadRecommendations

Audio player commands:
- search, select
- load
- playPause
- status
- createPlaylist, addRemoveInPlaylist, showPlaylists, switchVisibility
- like, follow
- showPreferredSongs
- repeat, shuffle
- forward, backward, next, prev

Page commands:
- changePage, printCurrentPage
- nextPage, previousPage

Artist commands:
- addAlbum, removeAlbum
- addEvent, removeEvent
- addMerch

Host commands:
- addPodcast, removePodcast
- addAnnouncement, removeAnnouncement

Statistics commands:
- getTop5Songs, getTop5Playlists
- getTop5Albums, getTop5Artists
- getAllUsers, getOnlineUsers
- wrapped

**Project structure**

List of packages:

> I. main

> II. constants

> III. contracts

> IV. user manager
 
> V. command handler
 
> VI. commands

> VII. audio player + audio models

> VIII. global statistics

> IX. admin

> X. artist

> XI. host

> XII. search bar

> XIII. page manager

> XIV. monetization

> XV. advertising

> XVI. notifications

# Implementation

+ The library and user manager are initialized at the start of the program.

+ The library contains all the songs, playlist, albums and podcasts. It also
stores the number of likes for each song.

+ After the commands are read from the input file, they are passed to the
command handler

+ The command handler stores all the commands name with their commands
classes in a dictionary, being able to execute the command without searching
for the name of the command in *constant time*.

+ Each normal user has a search bar, an audio player with created playlists,
liked songs and a page system implemented with a dictionary, that contains
the home page and the liked content page.

+ If a user creates a playlist, it is added to the library list of playlists.

+ If a user likes/unlikes a song, the library updates the number the likes of
that song.

+ The song and episode classes inherits from AudioFile, while the podcast and
playlist classes inheriths the AudioCollection class. The album class inherits
from playlist class.

+ Both AudioFile and AudioCollection inherits from the base class AudioEntity
which implements the playable and filtarable interfaces.

+ Each audio file remembers the remained time and last time it got updated.
The audio collections contains a list of audio files and stores the currently
playing audio file.

+ Every time a new command is given to the audio player, the audio entities
update their remaining time by substracting the time from the last update if
the audio was playing. If the remaining time is negative, it calculates the
next audio entity taking in consideration the repeat state and shuffle in case
of the playlists.

+ If a user is watching a podcast and searches a creator or audio, the
current episode is paused and the podcast is stored in a list of watched
podcasts. When the user starts playing the same podcast, the last episode
watched is resumed.

+ When a user searches for audio entities, the library returns the audio files
and then filtered. If the user searches for a audio creator the user manager
returns the artists or hosts and then filtered. Then, the search bar result
gets updated with the returned values. Both User class and AudioEntity class
implements the ISearchable interface.

+ The artist and host users inherit from the user class and have their own
constructor, in which the page system is initialized with their corresponding
pages.

+ The home page, liked content page, artist page and host page inherit from the
base class page of type *visitable*. Each of them have a content template and
implement the acceptVisit method accordingly.

+ When a normal user selects a content creator, it is redirected to its page.
The user gets the content of the page by *visiting* the page.

+ The user has a page history which remembers all the pages that the user
navigated to.

+ Each user has a dictionary for each statistic, which stores the number of
listens of an audio entity. The user also has a song history which stores the
artists listened and their songs listened by the user with their number of
listens with every type of account: free or premium. It is implemented with
a dictionary within a dictionary within a dictionary.

+ When an audio entity is played, it updates the user statistics and if it's
a song, it is added to the user history.

+ If a free user has an upcoming ad and the current song finishes, the user
the remaining time of the song is increased by the ad duration and the user
starts playing the ad. The user also pays all the artists listened until
then and resets the history for the free account.

+ Each content creator has a subscriber manager which notifies all the added
subscribers upon an update given by the artist/host. Each user subscribed
receives a notification and adds it to the notifications list.

+ When a user updates their recommendations, the last recommended audio entity
is set if a recommendation was found, otherwise it remains null.

# Design patterns

**Visitor** - The page class is a *visitable* type and each specific page
implements the acceptVisit method, while the user is a *visitor*, implementing
the visit method.

**Factory** - User factory returns a new user based on the type given 

**Strategy** - The command handler delegates executing the correct strategy
based on the command input given.

**Observer** - Content creators are the subject whom the users are subscribed.
Content creators have an subscriber manager which notifies all subscribers and
the normal users implement the Subscriber interface.

*Observation*: On the local machine, the checker results are 100/100.

## ---- Copyright Popescu Cristian-Emanuel 322CA 2023-2024 ---- ##
