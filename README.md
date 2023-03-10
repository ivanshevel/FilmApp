# FilmApp

## About Project
Simple application that displaying a list of newest film from public API.
Also added offline-first support and the ability to add movies to bookmarks.
There is a little of Dependency Injection in Hilt branch))
API used from [here](https://www.themoviedb.org/documentation/api).

## Architecture
MVVM

## Libraries and tools
- [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel)
- [LiveData](https://developer.android.com/topic/libraries/architecture/livedata)
- [DataBinding](https://developer.android.com/topic/libraries/data-binding/)
- [Navigation](https://developer.android.com/guide/navigation/)
- [Paging](https://developer.android.com/topic/libraries/architecture/paging/v3-overview)
- [Room](https://developer.android.com/training/data-storage/room)
- [Lifecycle](https://developer.android.com/topic/libraries/architecture/lifecycle)
- [ConstraintLayout](https://developer.android.com/develop/ui/views/layout/constraint-layout)
- [Retrofit](https://square.github.io/retrofit/)
- [OkHttp](https://square.github.io/okhttp/)
- [Coil](https://github.com/coil-kt/coil)

## FAQ
If you want to use this application, please add ```apikey.properties``` file to the root of the project.
The file must contain next line:
```
API_KEY="your_key_from_api_source"
```
## Animation
![target](https://github.com/ivanshevel/FilmApp/blob/main/animation.gif)