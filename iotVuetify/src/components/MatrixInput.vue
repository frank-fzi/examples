<template>
  <v-card>
    <v-card-title primary-title>
      <div>
        <h3 class="headline mb-0">{{title}}</h3>
      </div>
    </v-card-title>
    <v-flex>
      <center>
        <table style="border-collapse: separate; border-spacing: 2px;" border="1">
          <tbody>
            <tr>
              <td class="pixel-sel" id="ps0"></td>
              <td class="pixel-sel" id="ps1"></td>
              <td class="pixel-sel" id="ps2"></td>
              <td class="pixel-sel" id="ps3"></td>
              <td class="pixel-sel" id="ps4"></td>
            </tr>
            <tr>
              <td class="pixel-sel" id="ps5"></td>
              <td class="pixel-sel" id="ps6"></td>
              <td class="pixel-sel" id="ps7"></td>
              <td class="pixel-sel" id="ps8"></td>
              <td class="pixel-sel" id="ps9"></td>
            </tr>
            <tr>
              <td class="pixel-sel" id="ps10"></td>
              <td class="pixel-sel" id="ps11"></td>
              <td class="pixel-sel" id="ps12"></td>
              <td class="pixel-sel" id="ps13"></td>
              <td class="pixel-sel" id="ps14"></td>
            </tr>
            <tr>
              <td class="pixel-sel" id="ps15"></td>
              <td class="pixel-sel" id="ps16"></td>
              <td class="pixel-sel" id="ps17"></td>
              <td class="pixel-sel" id="ps18"></td>
              <td
                class="pixel-sel"
                id="ps19"
                style="background: rgb(255, 255, 255) none repeat scroll 0% 0%;"
              ></td>
            </tr>
            <tr>
              <td class="pixel-sel" id="ps20"></td>
              <td class="pixel-sel" id="ps21"></td>
              <td class="pixel-sel" id="ps22"></td>
              <td class="pixel-sel" id="ps23"></td>
              <td class="pixel-sel" id="ps24"></td>
            </tr>
            <tr>
              <td class="pixel-sel" id="ps25"></td>
              <td class="pixel-sel" id="ps26"></td>
              <td class="pixel-sel" id="ps27"></td>
              <td class="pixel-sel" id="ps28"></td>
              <td class="pixel-sel" id="ps29"></td>
            </tr>
            <tr>
              <td class="pixel-sel" id="ps30"></td>
              <td class="pixel-sel" id="ps31"></td>
              <td class="pixel-sel" id="ps32"></td>
              <td class="pixel-sel" id="ps33"></td>
              <td class="pixel-sel" id="ps34"></td>
            </tr>
          </tbody>
        </table>
      </center>
    </v-flex>
  </v-card>
</template>

<script>
export default {
  name: "MatrixInput",
  props: {
    title: String,
    brightness: 0
  },
  methods: {
    matrix: function() {
      var column = new Array(7);
      return column;
    },
    reverse: function(array) {
      var reverse = array.slice().reverse();
      return reverse;
    },
    intToRGB: function(int) {
      var r = int >> 16;
      var g = (int >> 8) & 0xff;
      var b = int & 0xff;
      var result = "#";
      result += this.byte2hex(r);
      result += this.byte2hex(g);
      result += this.byte2hex(b);
      return result;
    },
    byte2hex: function(byte) {
      var hex = Number(byte).toString(16);
      if (hex.length < 2) {
        hex = "0" + hex;
      }
      return hex;
    }
  }
};
</script>


