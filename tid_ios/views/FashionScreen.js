import React from 'react';
import {View, Text, TouchableOpacity, Image} from 'react-native';
import CardStack, {Card} from 'react-native-card-stack-swiper';
import {map} from 'lodash';
import profileIcon from '../assets/images/user.png';
import backImg from '../assets/images/back.png';
import goodImg from '../assets/images/green.png';
import badImg from '../assets/images/red.png';
import {fashionScreen} from './style';
import ImagePicker from 'react-native-image-picker';
import firestore from '@react-native-firebase/firestore';
import {uploadToFirebase} from '../firebase/posts';

export default class FashionScreen extends React.Component {
  state = {
    data: null,
  };

  async componentDidMount() {
    const documentSnapshot = await firestore()
      .collection('posts')
      .get();
    console.log('[documentSnapshot]');
    console.log(documentSnapshot.docs);
    this.setState({
      data: documentSnapshot.docs,
    });
  }

  _handleButtonPress = () => {
    const options = {
      title: 'Select Picture',
      storageOptions: {
        skipBackup: true,
        path: 'images',
      },
      maxWidth: 480,
      maxHeight: 480,
      quality: 0.5,
    };
    ImagePicker.launchImageLibrary(options, async res => {
      try {
        const response = await uploadToFirebase(res.uri);
        console.log('[firebase storage] succ');
        console.log(response);
      } catch (err) {
        console.log('[firebase storage] err');
        console.log(err);
      }
    });
  };
  render() {
    return (
      <View style={fashionScreen.container}>
        {/* <TouchableOpacity
          onPress={this._handleButtonPress}
          style={fashionScreen.plusWrapper}>
          <Text style={fashionScreen.plus}>{'+'}</Text>
        </TouchableOpacity> */}
        <TouchableOpacity style={fashionScreen.profileIconWrapper}>
          <Image style={fashionScreen.profileIcon} source={profileIcon} />
        </TouchableOpacity>
        <CardStack
          style={fashionScreen.cardStack}
          renderNoMoreCards={() => (
            <Text
              style={{
                fontWeight: '700',
                fontSize: 18,
                color: 'gray',
              }}>
              No more cards :(
            </Text>
          )}
          ref={swiper => {
            this.swiper = swiper;
          }}
          verticalSwipe={false}
          onSwiped={() => console.log('onSwiped')}
          onSwipedLeft={() => console.log('onSwipedLeft')}>
          {map(this.state.data, (v, i) => (
            <Card key={i} style={[fashionScreen.card, fashionScreen.card1]}>
              <Image
                source={{
                  uri: v.data().imgUrl,
                }}
                style={{
                  width: '100%',
                  height: '100%',
                }}
              />
            </Card>
          ))}
        </CardStack>

        <View style={fashionScreen.footer}>
          <View style={fashionScreen.buttonContainer}>
            <TouchableOpacity
              style={[fashionScreen.button, fashionScreen.thumbs]}
              onPress={() => {
                this.swiper.swipeLeft();
              }}>
              <Image
                source={badImg}
                resizeMode={'contain'}
                style={{height: 40, width: 40}}
              />
            </TouchableOpacity>
            {/* <TouchableOpacity
              style={[fashionScreen.button, fashionScreen.orange]}
              onPress={() => {
                this.swiper.goBackFromLeft();
              }}>
              <Image
                source={backImg}
                resizeMode={'contain'}
                style={{height: 32, width: 32, borderRadius: 5}}
              />
            </TouchableOpacity> */}
            <TouchableOpacity
              style={[fashionScreen.button, fashionScreen.thumbs]}
              onPress={() => {
                this.swiper.swipeRight();
              }}>
              <Image
                source={goodImg}
                resizeMode={'contain'}
                style={{height: 40, width: 40}}
              />
            </TouchableOpacity>
          </View>
        </View>
      </View>
    );
  }
}
