import React from 'react';
import {View, Text, TouchableOpacity, Image} from 'react-native';
import CardStack, {Card} from 'react-native-card-stack-swiper';
import backImg from '../assets/images/back.png';
import goodImg from '../assets/images/green.png';
import badImg from '../assets/images/red.png';
import {styles} from './style';

export default function FashionScreen() {
  return (
    <View style={styles.container}>
      <CardStack
        style={styles.content}
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
        <Card style={[styles.card]}>
          <Text style={styles.label}>A</Text>
        </Card>
        <Card style={[styles.card, styles.card2]}>
          <Text style={styles.label}>B</Text>
        </Card>
        <Card style={[styles.card, styles.card1]}>
          <Text style={styles.label}>C</Text>
        </Card>
        <Card style={[styles.card, styles.card2]}>
          <Text style={styles.label}>D</Text>
        </Card>
        <Card style={[styles.card, styles.card1]}>
          <Text style={styles.label}>E</Text>
        </Card>
      </CardStack>

      <View style={styles.footer}>
        <View style={styles.buttonContainer}>
          <TouchableOpacity
            style={[styles.button, styles.red]}
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
            style={[styles.button, styles.orange]}
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
            style={[styles.button, styles.green]}
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
