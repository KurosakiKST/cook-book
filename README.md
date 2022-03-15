# CookBook

## Installation
Clone this repository and import into **Android Studio**
```bash
git clone git@github.com:KurosakiKST/cook-book.git
```

## Configuration
### Keystores:
Create `app/keystore.gradle` with the following info:
```gradle
ext.key_alias='...'
ext.key_password='...'
ext.store_password='...'
```
And place both keystores under `app/keystores/` directory:
- `playstore.keystore`
- `stage.keystore`

## Developers
This project is mantained by:
* [Kyaw Sithu](https://github.com/KurosakiKST)

## API used
* Retrofit2
* Glide
* Smart Tab Layout
* Country Code Picker
* Imageslider
* Expandable Textview
* Social Login
* Exoplayer

## APP icon credit goes to
* "https://www.flaticon.com/free-icons/kitchen"
* <a href="https://www.flaticon.com/free-icons/kitchen" title="kitchen icons">Kitchen icons created by Freepik - Flaticon</a>
