<template>
<v-card>
  <v-card-title primary-title>
    <div>
      <h3 class="headline mb-0">{{title}}</h3>
    </div>
  </v-card-title>
  <v-flex>Farbe wählen: <input type="color" v-model="color">
    <input v-model="color"></v-flex>
  <v-flex><center>
  <table style="border-collapse: separate; border-spacing: 0px;" border="0">
    <tbody>
    <tr v-for="(row, r_index) in transpose(matrix)" :key="r_index">
      <td v-for="(data, d_index) in row" :key="d_index" :bgcolor=intToRGB(data) class=pixel-sel @click="setPixel(d_index, r_index)"></td>
    </tr>
    </tbody>
  </table></center>
  </v-flex>
</v-card>    
</template>

<script>
export default {
  name: 'LightMatrix',
  props: {
    title: String,
    matrix: null,
    brightness: 0,
    table: null
  },
  data: () => ({
    color: "#0000ff"
  }),
  methods : {
    transpose: function (array, arrayLength = 7) {
        var newArray = [];
        for(var i = 0; i < array.length; i++){
            newArray.push([]);
        };

        for(var i = 0; i < array.length; i++){
            for(var j = 0; j < arrayLength; j++){
                newArray[6-j].push(array[i][j]);
            };
        };

        return newArray;
    },
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
    setPixel: function(x, y) {  
      var colorcode = this.hex2rgba(this.color);
      var request = {request: "SET_PIXEL", x:x, y:6-y, colorcode:colorcode}    
      this.send(request)
    },
    send: function (request) {
      console.log(request)
      this.$socket.send(JSON.stringify(request))
    },
    console : function (c_index, r_index) {
      console.log(c_index, r_index);
    },
    reverse : function (array) {
      var reverse = array.slice().reverse();
      return reverse;
    },
    intToRGB : function(int) {
      var r = int >> 16;
      var g = int >> 8 & 0xFF;
      var b = int & 0xFF;
      var result = "#";
      result += this.byte2hex(r);
      result += this.byte2hex(g);
      result += this.byte2hex(b);
      return (result);
    },
    byte2hex : function(byte) {
      var hex = Number(byte).toString(16);
      if (hex.length < 2) {
        hex = "0" + hex;
      }
      return hex;
    }
  }
}
</script>

<style>
  .color-sel { width: 24px; height: 24px; text-align: center;}
  .pixel-sel { width: 36px; height: 36px;}
</style>


