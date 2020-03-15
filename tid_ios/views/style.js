import {StyleSheet} from 'react-native';

export const styles = StyleSheet.create({
  container: {
    flex: 1,
  },
  content: {
    flex: 5,
    alignItems: 'center',
    justifyContent: 'center',
  },
  card: {
    width: 320,
    height: 470,
    backgroundColor: '#FE474C',
    borderRadius: 5,
    shadowColor: 'rgba(0,0,0,0.5)',
    shadowOffset: {
      width: 0,
      height: 1,
    },
    shadowOpacity: 0.5,
  },
  card1: {
    backgroundColor: '#FE474C',
  },
  card2: {
    backgroundColor: '#FEB12C',
  },
  label: {
    lineHeight: 400,
    textAlign: 'center',
    fontSize: 55,
    fontFamily: 'System',
    color: '#ffffff',
    backgroundColor: 'transparent',
  },
  footer: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
  },
  buttonContainer: {
    width: 220,
    flexDirection: 'row',
    justifyContent: 'space-between',
  },
  button: {
    shadowColor: 'rgba(0,0,0,0.3)',
    shadowOffset: {
      width: 0,
      height: 1,
    },
    shadowOpacity: 0.5,
    backgroundColor: '#fff',
    alignItems: 'center',
    justifyContent: 'center',
    zIndex: 0,
  },
  orange: {
    width: 55,
    height: 55,
    borderWidth: 6,
    borderColor: 'rgb(246,190,66)',
    borderRadius: 55,
    marginTop: -15,
  },
  green: {
    width: 75,
    height: 75,
    backgroundColor: '#fff',
    borderRadius: 75,
    borderWidth: 6,
    borderColor: '#01df8a',
  },
  red: {
    width: 75,
    height: 75,
    backgroundColor: '#fff',
    borderRadius: 75,
    borderWidth: 6,
    borderColor: '#fd267d',
  },
});

export const loginScreen = StyleSheet.create({
  container: {
    flex: 1,
    paddingHorizontal: 20,
    alignItems: 'center',
    justifyContent: 'center',
    backgroundColor: 'white',
  },
  titleWrapper: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'flex-end',
  },
  loginTitle: {
    fontSize: 25,
    fontWeight: 'bold',
  },
  loginWrapper: {
    width: '100%',
    height: 300,
    marginHorizontal: 20,
    paddingHorizontal: 20,
    backgroundColor: 'white',
  },
  smallText: {
    fontSize: 11,
    color: 'grey',
    marginVertical: 10,
  },
  textInput: {
    width: '100%',
    height: 40,
    borderColor: 'grey',
    borderWidth: 1,
    paddingLeft: 10,
  },
  forgotPassword: {
    width: '100%',
    height: 50,
    alignItems: 'flex-end',
    marginTop: 10,
  },
  signInBtn: {
    alignSelf: 'center',
    alignItems: 'center',
    justifyContent: 'center',
    width: 200,
    height: 35,
    backgroundColor: '#FC5185',
    borderRadius: 5,
  },
  signUpBtn: {
    alignSelf: 'center',
    marginTop: 10,
  },
});
