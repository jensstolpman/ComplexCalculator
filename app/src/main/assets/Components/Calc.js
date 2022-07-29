document.write(`
    <script type="text/x-template" id = "calc-template">
      <div id = "calculator">
          <calc-menu></calc-menu>
          <div class="calculator-logs">
            <span v-for="cplx in stack">
                <span v-if="!isMantissa(cplx)">{{ cplx }}</span>
            </span>
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
        height: 10.2vh;
        display: flex;
        position: relative;
        overflow: hidden;
        align-items: flex-end;
        flex-direction: column;
        justify-content: flex-end;
        padding: 0.5vh;
    }
    #calculator .calculator-logs span {
        color: #D4D4D2;
        display: block;
        font-size: 3vh;
        text-align: right;
        margin-top: 0.2vh;
        line-height: 1;
        font-weight: lighter;
        padding-right: 0.8vh;
    }
    #calculator .calculator-input {
      color: #D4D4D2;
      width: 100%;
      border: none;
      padding: 0vh 1vh 0vh 1vh;
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
        beforeMount: function(){
           this.getMantissa();
        },
        stackCounter: 1,
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
            isMantissa: function (cplx) {
                var result = 0;
                var stackSize = this.stack.length;
                if (this.stackCounter<stackSize){
                    this.stackCounter++;
                } else {
                    if (this.newEnter==1) {
                        result = 1;
                    }
                    this.stackCounter = 1;
                }
                return result;
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
               var address = 'http://localhost:' + this.$root.port + '/Calculate';
               const headers = {
                   'Access-Control-Allow-Origin': address
               };
               Vue.axios.get(address, this.$data, { headers })
                .then(response => {
                                        this.value = response.data.mantissa;
                                        this.stack = response.data.stack;
                                        this.newEnter = 1;
                                        this.stackCounter = 1;
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
                                        this.stack = response.data.stack;
                                        this.newEnter = 1;
                                  }
                     )
                .catch(e => {
                  this.errors.push(e.message)
                })
            },
        }
});
