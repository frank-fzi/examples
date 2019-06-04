<template>
<v-card>
    <v-card-title primary-title>
    <div>
        <h3 class="headline mb-0">{{title}}</h3>
    </div>
    </v-card-title>
    <v-flex>Farbe wählen: <input type="color" v-model="color">
    <input v-model="color"></v-flex>
    <v-flex><v-btn block round color="primary" @click="column">Spalte</v-btn></v-flex>
    <v-flex><v-btn block round color="primary" @click="read">Bild</v-btn></v-flex>
    <v-flex><v-switch
      label="Aktualisieren"
      @change="refresh($event)"
    ></v-switch></v-flex>
    <v-flex><v-slider
        v-model="brightness"
        thumb-label="always"
        label="Helligkeit"
        max=255
        @change="setBrightness($event)"
    ></v-slider></v-flex> 
    <v-flex><v-slider
        v-model="fps"
        thumb-label="always"
        label="FPS"
        min=1
        max=100
        @change="setFps($event)"
    ></v-slider></v-flex>
</v-card>
</template>

<script>
var axios = require('axios');
var Jimp = require('jimp');

export default {
  name: 'ActionButtons',
  props: {
    title: String,
    brightness: 0,    
    fps: 0,
    size: 7
  },
  data: () => ({
    color: "#00ff00",
    displayModes: ['FRONT_ONLY', 'BACK_ONLY', 'COPY', 'MIRROW_TO_BACK', 'MIRROW_TO_FRONT'],
    displayMode: 'MIRROW_TO_BACK'
  }),
  methods: {
    hex2rgba: function (hex) {      
      if(/^#([A-Fa-f0-9]{3}){1,2}$/.test(hex)){
        var rgba;
        rgba = hex.substring(1).split('');
        if(rgba.length == 3){
            rgba = [rgba[0], rgba[0], rgba[1], rgba[1], rgba[2], rgba[2]];
        }
        rgba = '0x'+ rgba.join('') + 'ff';
        return parseInt(rgba);
      }
      throw new Error('Bad Hex');
    },
    send: function (request) {
      console.log(request)
      this.$socket.send(JSON.stringify(request))
    },
    message: function (message) {
      var request = {request: "SET_MESSAGE", message: message}
      this.send(request)
    },
    column: function() {  
      var column = new Array(7);
      for(var i=0;i<7;i++) {column[i] = this.hex2rgba(this.color);}
      var request = {request: "SET_COLUMN", column: column, displayMode: this.displayMode}    
      this.send(request)
    },
    refresh: function(payload = true){
      if(payload) {
        this.$socket.send("subscribe");
      } else {
        this.$socket.send("unsubscribe");
      }
    },
    setBrightness: function (brightness) {
      var request = {
        request: "SET_BRIGHTNESS", 
        brightness: brightness
      }
      this.send(request)
    },
    setFps: function (fps) {
      var request = {
        request: "SET_FPS", 
        fps: fps
      }
      this.send(request)
    },
    eight: function () {
      axios.get('http://192.168.2.33/8')
    },
    red: function () {
      axios.get('http://192.168.2.33/red')
    },
    colortest: function() {
      axios.get('http://192.168.2.33/colortest')
    },
    read: async function() {
      Jimp.read('./knutschi.bmp')
      .then(image => {        
        image.resize(Jimp.AUTO, 7);
        console.log("width: " + image.getWidth());
        var limit = image.getWidth() // < 500 ? image.getWidth() : 500;
        for(var x=0;x<limit;x++){
          var column = new Array(30)
          for(var y=0;y<30;y++){
            column[y] = image.getPixelColor(x,y)             
          }
          var request = {request: "SET_COLUMN", column: column}
          this.send(request)
          //await this.sleep(500);
        }
      })
      .catch(err => {
        console.log(err);
      });
    },
    sleep: function sleep(ms) {
      return new Promise(resolve => setTimeout(resolve, ms));
    }
  }
}
</script>