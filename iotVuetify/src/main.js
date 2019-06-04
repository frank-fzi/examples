import Vue from 'vue'
import './plugins/vuetify'
//import VueSocketIO from 'vue-socket.io'
//import VueWebsocket from 'vue-websocket'
import VueTouch from 'vue-touch'
import App from './App.vue'

import VueNativeSock from 'vue-native-websocket'
Vue.use(VueNativeSock, 'ws://iotMatrix:81', {
  format: 'json',
  reconnection: true
})

Vue.use(VueTouch);

Vue.config.productionTip = false

new Vue({
  render: h => h(App),
}).$mount('#app')
