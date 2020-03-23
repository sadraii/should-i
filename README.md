![Main UI](demo/main_ui.png)

# Should I?

A photo-sharing app where you can take a picture, add a caption, and have others help you decide if you should do something.

<a href='https://play.google.com/store/apps/details?id=com.sadraii.shouldi&pcampaignid=pcampaignidMKT-Other-global-all-co-prtnr-py-PartBadge-Mar2515-1'><img alt='Get it on Google Play' src='https://play.google.com/intl/en_us/badges/static/images/badges/en_badge_web_generic.png' width='250'/></a>

### Frameworks and Libraries

- **LiveData** for updating the UI with changes
- **Navigation** to implement Single Activity architecture
- **Room** for local DB
- **ViewModel** for MVVC architecture
- **CameraX** to handle taking pictures
- **Glide** for image loading
- **Material** for UI components wherever possible

### Back-end

The app uses the following Firebase components:

- **Firestore** for captions and metadata
- **Storage** to save the pictures
- **AuthUI** for authentication

## License

**Should I** is licensed under Apache License 2.0.

See [LICENSE](LICENSE)
