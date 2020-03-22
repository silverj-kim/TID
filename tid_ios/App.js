import * as React from 'react';
import {View, Text, StyleSheet, TouchableOpacity, Image} from 'react-native';
import {NavigationContainer, CommonActions} from '@react-navigation/native';
import {createStackNavigator} from '@react-navigation/stack';
import FashionScreen from './views/FashionScreen';
import LoginScreen from './views/LoginScreen';
import auth from '@react-native-firebase/auth';
import {isEmpty} from 'lodash';
import {storeData} from './utils/async-storage-manager';

const Stack = createStackNavigator();

class App extends React.Component {
  state = {
    token: null,
  };

  componentDidMount() {
    this.unsubscriber = auth.onAuthStateChanged(user => {
      if (!isEmpty(user)) {
        this._loadUser(user);
      }
    });
  }

  componentWillUnmount() {
    if (this.unsubscriber) {
      this.unsubscriber();
    }
  }

  _loadUser = async user => {
    try {
      const {uid} = user._user;
      await storeData('token', uid);
      this.setState({
        token: uid,
      });
    } catch (err) {
      console.log(err);
    }
  };

  render() {
    return (
      <NavigationContainer ref={this.navigationRef}>
        <Stack.Navigator>
          {this.state.token ? (
            <Stack.Screen name="Fashion" component={FashionScreen} />
          ) : (
            <Stack.Screen name="Login" component={LoginScreen} />
          )}
        </Stack.Navigator>
      </NavigationContainer>
    );
  }
}

export default App;
