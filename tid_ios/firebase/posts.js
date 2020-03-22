import firebase from 'react-native-firebase';

export function uploadToFirebase(uri) {
  return new Promise(async (resolve, reject) => {
    const storageRef = firebase.storage().ref();
    const fileName = uri.split('images/').pop();
    console.log(uri);
    console.log(fileName);

    storageRef
      .child(`images/${fileName}`)
      .putFile(uri)
      .on(
        firebase.storage.TaskEvent.STATE_CHANGED,
        snapshot => {
          // let state = {};
          // state = {
          //   ...state,
          //   progress: (snapshot.bytesTransferred / snapshot.totalBytes) * 100,
          // };
          if (snapshot.state === firebase.storage.TaskState.SUCCESS) {
            // const allImages = this.state.images;
            // allImages.push(snapshot.downloadURL);
            // state = {
            //   ...state,
            //   uploading: false,
            //   imgSource: '',
            //   imageUri: '',
            //   progress: 0,
            //   images: allImages,
            // };
            // AsyncStorage.setItem('images', JSON.stringify(allImages));
            resolve(snapshot);
          }
          // this.setState(state);
        },
        error => {
          reject(error);
        },
      );
  });
}
