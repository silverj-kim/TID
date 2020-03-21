import React from 'react';
import {View, Text, TouchableOpacity, Image} from 'react-native';
import CardStack, {Card} from 'react-native-card-stack-swiper';
import profileIcon from '../assets/images/user.png';
import backImg from '../assets/images/back.png';
import goodImg from '../assets/images/green.png';
import badImg from '../assets/images/red.png';
import {fashionScreen} from './style';
import ImagePicker from 'react-native-image-picker';

export default function FashionScreen() {
  _handleButtonPress = () => {
    const options = {
      title: 'Select TID image',
      storageOptions: {
        skipBackup: true,
        path: 'images',
      },
    };
    ImagePicker.launchImageLibrary(options, res => {
      console.log(res);
    });
  };
  return (
    <View style={fashionScreen.container}>
      <TouchableOpacity
        onPress={this._handleButtonPress}
        style={fashionScreen.plusWrapper}>
        <Text style={fashionScreen.plus}>{'+'}</Text>
      </TouchableOpacity>
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
        <Card style={[fashionScreen.card]}>
          <Text style={fashionScreen.label}>A</Text>
        </Card>
        <Card style={[fashionScreen.card, fashionScreen.card2]}>
          <Text style={fashionScreen.label}>B</Text>
        </Card>
        <Card style={[fashionScreen.card, fashionScreen.card1]}>
          <Text style={fashionScreen.label}>C</Text>
        </Card>
        <Card style={[fashionScreen.card, fashionScreen.card2]}>
          <Text style={fashionScreen.label}>D</Text>
        </Card>
        <Card style={[fashionScreen.card, fashionScreen.card1]}>
          <Text style={fashionScreen.label}>E</Text>
        </Card>
      </CardStack>

      <View style={fashionScreen.footer}>
        <View style={fashionScreen.buttonContainer}>
          <TouchableOpacity
            style={[fashionScreen.button, fashionScreen.red]}
            onPress={() => {
              this.swiper.swipeLeft();
            }}>
            <Image
              source={badImg}
              resizeMode={'contain'}
              style={{height: 62, width: 62}}
            />
          </TouchableOpacity>
          <TouchableOpacity
            style={[fashionScreen.button, fashionScreen.orange]}
            onPress={() => {
              this.swiper.goBackFromLeft();
            }}>
            <Image
              source={backImg}
              resizeMode={'contain'}
              style={{height: 32, width: 32, borderRadius: 5}}
            />
          </TouchableOpacity>
          <TouchableOpacity
            style={[fashionScreen.button, fashionScreen.green]}
            onPress={() => {
              this.swiper.swipeRight();
            }}>
            <Image
              source={goodImg}
              resizeMode={'contain'}
              style={{height: 62, width: 62}}
            />
          </TouchableOpacity>
        </View>
      </View>
    </View>
  );
}
