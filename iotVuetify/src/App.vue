<template>
  <v-app>
    <v-toolbar color="indigo" dark fixed app>
      <v-toolbar-title>iotVuetify</v-toolbar-title>
    </v-toolbar>
    <v-content>
      <v-container fluid grid-list-lg>
        <v-layout column>
          <v-flex>
            <v-layout justify-center wrap>
              <light-matrix 
                title="Anzeige"
                v-bind ="this.front"
              ></light-matrix>
              <action-buttons
                title="Aktionen"
                v-bind:brightness ="this.brightness"
                v-bind:fps="this.fps"
              ></action-buttons>
            </v-layout>
          </v-flex>
        </v-layout>
      </v-container>
    </v-content>
    <v-footer color="indigo" app>
      <span class="white--text">&copy; {{copyright}}</span>
    </v-footer>
  </v-app>
</template>

<script>
import LightMatrix from './components/LightMatrix.vue'
import ActionButtons from './components/ActionButtons.vue'
import MatrixInput from './components/MatrixInput.vue'

export default {
  components: {
    LightMatrix,
    ActionButtons,
  },    
  data: () => ({
    copyright: 'Matthias Frank 24. Dezember 2018',
    brightness: 0,
    fps: 0,
    brightnessFront: 0,
    brightnessBack: 0,
    size: 30,
    count: 4,
    front: null,
    back: null,
  }),
  methods: {    
    matrix2table: function(matrix) {
      var table = new Array(7)
      var copy = matrix.slice()
      for(var x=0; x<7; x++) {
        var row = new Array(7)
        for(var y=0; y<7; y++) {
          row[x] = copy[x,y];
        }
        table[y] = row
      }
      return table
    },
    parse: function(string) {
      if(string.charAt(0) == '{') {
        var message = JSON.parse(string);
        if (message.success) {          
          switch (message.response) {
            case "GET_STATE" : {
              this.brightness = message.brightness
              this.fps = message.fps
              this.front = message.front
              this.table = this.matrix2table(message.front.matrix)
              this.back = message.back
              break;
            }
            default : {
              console.log(message)
            }
          }
        } else {
          console.log(message)
        }        
      } else {
        console.log(string);
      }
    }
  },
  mounted () {
    this.$options.sockets.onmessage = (message) => this.parse(message.data)
  }
}
</script>