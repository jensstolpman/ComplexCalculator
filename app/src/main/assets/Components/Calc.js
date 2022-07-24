document.write(`
    <script type="text/x-template" id = "calc-template">
      <div id = "calculator">
          <calc-menu></calc-menu>
          <div class="calculator-logs">
            <span v-for="cplx in stack">{{ cplx }}</span>
          </div>

          <input type="string" class="calculator-input" v-model="value" @keyup.enter="getResult()" readonly>

          <div class="calculator-row">
            <div class="calculator-col">
              <button class="calculator-btn gray action" @click="clear()">C</button>
            </div>
            <div class="calculator-col">
              <button class="calculator-btn gray action" @click="del()">del</button>
            </div>
            <div class="calculator-col">
              <button class="calculator-btn gray action" @click="addExpression('i')">i</button>
            </div>
            <div class="calculator-col">
              <button class="calculator-btn accent action" @click="calculate('divide')">/</button>
            </div>
          </div>
          <div class="calculator-row">
            <div class="calculator-col">
              <button class="calculator-btn" @click="addExpression(7)">7</button>
            </div>
            <div class="calculator-col">
              <button class="calculator-btn" @click="addExpression(8)">8</button>
            </div>
            <div class="calculator-col">
              <button class="calculator-btn" @click="addExpression(9)">9</button>
            </div>
            <div class="calculator-col">
              <button class="calculator-btn accent action" @click="calculate('multiply')">*</button>
            </div>
          </div>
          <div class="calculator-row">
            <div class="calculator-col">
              <button class="calculator-btn" @click="addExpression(4)">4</button>
            </div>
            <div class="calculator-col">
              <button class="calculator-btn" @click="addExpression(5)">5</button>
            </div>
            <div class="calculator-col">
              <button class="calculator-btn" @click="addExpression(6)">6</button>
            </div>
            <div class="calculator-col">
              <button class="calculator-btn accent action" @click="calculate('subtract')">-</button>
            </div>
          </div>
          <div class="calculator-row">
            <div class="calculator-col">
              <button class="calculator-btn" @click="addExpression(1)">1</button>
            </div>
            <div class="calculator-col">
              <button class="calculator-btn" @click="addExpression(2)">2</button>
            </div>
            <div class="calculator-col">
              <button class="calculator-btn" @click="addExpression(3)">3</button>
            </div>
            <div class="calculator-col">
              <button class="calculator-btn accent action" @click="calculate('add')">+</button>
            </div>
          </div>
          <div class="calculator-row">
            <div class="calculator-col">
              <button class="calculator-btn" @click="addExpression(0)">0</button>
            </div>
            <div class="calculator-col">
              <button class="calculator-btn action" @click="addExpression('.')">.</button>
            </div>
            <div class="calculator-col wide">
              <button class="calculator-btn accent action" @click="enter()">Enter</button>
            </div>
          </div>
      </div>
    </script>
    <script type="text/x-template" id = "calculator-template">
      <calculator>
      </calculator>
    </script>

    <style>
    #calculator {
          height: 100%;
          min-height: 100%;
          display: flex;
          flex-direction: column;
          margin: 0 auto;
          padding: 0;
          flex-direction: column;
          background-color: #2f2f31;
    }
    #calculator .calculator-logs {
      height: 80px;
      display: flex;
      position: relative;
      overflow: hidden;
      align-items: flex-end;
      flex-direction: column;
      justify-content: flex-end;
    }
    #calculator .calculator-logs:before {
      top: 0;
      left: 0;
      right: 0;
      height: 48px;
      content: '';
      z-index: 5;
      position: absolute;
      background: linear-gradient(to bottom, #2f2f31, rgba(47, 47, 49, 0));
    }
    #calculator .calculator-logs span {
      color: #D4D4D2;
      opacity: .75;
      display: block;
      font-size: .8rem;
      text-align: right;
      margin-top: .4rem;
      line-height: 1;
      font-weight: lighter;
    }
    #calculator .calculator-input {
      color: #D4D4D2;
      width: 100%;
      border: none;
      padding: .8rem;
      display: block;
      font-size: 6vh;
      background: none;
      text-align: right;
      font-weight: lighter;
    }
    #calculator .calculator-input:focus, #calculator .calculator-input:active {
      outline: none;
    }

    #calculator .calculator-row {
      display: flex;
      flex:1;
      padding: 0;
      justify-content: space-around;
    }
    #calculator .calculator-row .calculator-col {
      flex: 1;
      box-shadow: 0 0 0 1px #2f2f31;
    }
    #calculator .calculator-row .calculator-col.wide {
      flex: 2;
    }
    #calculator .calculator-btn {
      width: 100%;
      height: 100%;
      color: #D4D4D2;
      border: none;
      cursor: pointer;
      padding: .8rem;
      outline: none;
      font-size: 5vh;
      transition: all .3s ease-in-out;
      font-weight: 200;
      justify-content: center;
      background-color: #616163;
    }

    #calculator .calculator-btn.accent {
      background-color: #f49e3f;
      color: #fff;
    }
    #calculator .calculator-btn.gray {
      background-color: #424345;
    }
    </style>
`);

Vue.component('calculator', {
        template: '#calc-template',
        data() {
            return {
                value: '0',
                command: '',
                logs: [],
                errors: [],
                stack: [],
                newEnter: 1,
            }
        },
        mounted: function(){
           this.getMantissa();
        },
        methods: {
            addExpression: function (e) {
                if (this.newEnter==1){
                    this.value = '';
                    this.newEnter = 0;
                }
                this.value += e;
            },
            getResult: function () {
                var log = this.value;
                this.value = eval(this.value);
                this.logs.push(log + ("=" + this.value));
            },
            clear: function () {
                this.value = 0;
            },
            del: function () {
                this.value = this.value.slice(0, -1);
            },
            enter: function () {
               this.calculate('enter')
            },
            getMantissa: function () {
               var address = 'http://127.0.0.1:' + this.$root.port + '/Calculate';
               const headers = {
                   'Access-Control-Allow-Origin': address
               };
               Vue.axios.get(address, this.$data, { headers })
                .then(response => {
                                        this.value = response.data.mantissa;
                                        this.stack = response.data.stack;
                                        this.newEnter = 1;
                                  }
                     )
                .catch(e => {
                  this.errors.push(e.message)
                })
            },
            calculate: function (e) {
               this.command=e;
               const address = 'http://localhost:' + this.$root.port + '/Calculate';
               const headers = {
                   'Access-Control-Allow-Origin': address
               };
               Vue.axios.post(address, this.$data, { headers })
                .then(response => {
                                        this.value = response.data.value;
                                        this.newEnter = 1;
                                  }
                     )
                .catch(e => {
                  this.errors.push(e.message)
                })
            },
        }
});
