import * as React from 'react';
import {View, Text, StyleSheet, TouchableOpacity, Image} from 'react-native';
import {NavigationContainer} from '@react-navigation/native';
import {createStackNavigator} from '@react-navigation/stack';
import FashionScreen from './views/FashionScreen';
import LoginScreen from './views/LoginScreen';
import firebase from 'react-native-firebase';

const Stack = createStackNavigator();

class App extends React.Component {
  state = {
    user: null,
  };

  componentDidMount() {
    this.unsubscriber = firebase.auth().onAuthStateChanged(user => {
      this.setState({user});
    });
  }

  componentWillUnmount() {
    if (this.unsubscriber) {
      this.unsubscriber();
    }
  }

  render() {
    return (
      <NavigationContainer>
        <Stack.Navigator>
          <Stack.Screen name="Login" component={LoginScreen} />
          <Stack.Screen name="Fashion" component={FashionScreen} />
        </Stack.Navigator>
      </NavigationContainer>
    );
  }
}

export default App;
