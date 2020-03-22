import React, {useState} from 'react';
import {View, Text, TextInput, TouchableOpacity, Alert} from 'react-native';
import {loginScreen} from './style';
import auth from '@react-native-firebase/auth';
import {storeData, retrieveData} from '../utils/async-storage-manager';

export default function LoginScreen({navigation}) {
  const [email, onChangeEmail] = useState('');
  const [password, onChanegePassword] = useState('');
  const regExp = /^[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*\.[a-zA-Z]{2,3}$/i;
  const signin = async () => {
    if (email && password) {
      if (!regExp.test(email)) {
        Alert.alert('올바른 이메일을 입력해주세요.');
      } else {
        try {
          const res = await auth().signInWithEmailAndPassword(email, password);
          console.log(res);
          console.log(res.user);
          console.log(res.user._user);
          navigation.navigate('Fashion');
        } catch (err) {
          if (err.code) {
            Alert.alert(err.code);
          } else {
            Alert.alert('sign up failed');
          }
        }
      }
    } else {
      Alert.alert('이메일과 패스워드를 입력해주세요.');
    }
  };
  const signup = async () => {
    try {
      const res = await auth().createUserWithEmailAndPassword(email, password);
    } catch (err) {
      if (err.code) {
        Alert.alert(err.code);
      } else {
        Alert.alert('sign up failed');
      }
    }
  };
  const findPassword = async () => {
    try {
      const res = await auth().sendPasswordResetEmail(email);
      Alert.alert('check your mail');
    } catch (err) {
      Alert.alert('failed to send reset password mail');
    }
  };

  return (
    <View style={loginScreen.container}>
      <View style={loginScreen.titleWrapper}>
        <Text style={loginScreen.loginTitle}>{'LOGIN'}</Text>
      </View>
      <View style={loginScreen.loginWrapper}>
        <Text style={loginScreen.smallText}>{'Email'}</Text>
        <TextInput
          placeholder={'example@gmail.com'}
          textContentType={'emailAddress'}
          style={loginScreen.textInput}
          onChangeText={text => onChangeEmail(text)}
          value={email}
        />
        <Text style={loginScreen.smallText}>{'Password'}</Text>
        <TextInput
          placeholder={'********'}
          textContentType={'password'}
          style={loginScreen.textInput}
          onChangeText={text => onChanegePassword(text)}
          value={password}
          secureTextEntry={true}
        />
        <TouchableOpacity
          onPress={findPassword}
          style={loginScreen.forgotPassword}>
          <Text style={loginScreen.smallText}>{'Forgot Password?'}</Text>
        </TouchableOpacity>
        <TouchableOpacity onPress={signin} style={loginScreen.signInBtn}>
          <Text
            style={{
              color: 'white',
            }}>
            {'SIGN IN'}
          </Text>
        </TouchableOpacity>
        <TouchableOpacity onPress={signup} style={loginScreen.signUpBtn}>
          <Text style={loginScreen.smallText}> {'계정이 없으신가요?'} </Text>
        </TouchableOpacity>
      </View>
      <View
        style={{
          flex: 1,
        }}
      />
    </View>
  );
}
