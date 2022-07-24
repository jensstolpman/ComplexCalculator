document.write(`
    <script src="./Components/SelectDigits.js"></script>
    <script type="text/x-template" id = "conf-template">
      <div id = "configurator">
        <calc-menu></calc-menu>
        <div>
            <button class="conf-btn" > </button>
        </div>
        <div>
            <button class="conf-btn">{{ $t("message.digits") }}</button>
            <button class="conf-btn">
                <select-digits
                      ref="selectDigits"
                      :options="[1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12]"
                      :default="this.decimals"
                      class="select"
                >
                </select-digits>
            </button>
        </div>
        <div>
            <button class="conf-btn" > </button>
        </div>
        <div>
            <button class="conf-btn" @click="ok()">{{ $t("message.ok") }}</button>
            <button class="conf-btn" > </button>
            <button class="conf-btn" @click="cancel()">{{ $t("message.cancel") }}</button>
        </div>
      </div>
    </script>

    <script type="text/x-template" id = "configurator-template">
        <configurator>
        </configurator>
    </script>
    <style>
        #configurator .conf-btn {
            color:#D4D4D2;
            background-color: #2f2f31;
            border: none;
            font-size: 3vh;
        }
    </style>
`);


Vue.component('configurator', {
        template: '#conf-template',
        data() {
            return {
                decimals: 4,
            }
        },
        mounted: function (){
            this.getConfig();
        },
        methods: {
            getConfig: function () {
               const confAddress = 'http://127.0.0.1:' + this.$root.port + '/Configure';
               const headers = {
                 'Access-Control-Allow-Origin': confAddress
               };
               Vue.axios.get(confAddress, this.$data, { headers })
               .then(response => {
                                    this.decimals = response.data.decimals;
                                    this.$refs.selectDigits.selected = this.decimals;
                                  })
                .catch(e => {
                  this.errors.push(e.message)
                })
            },
            readData: function () {
                this.decimals = this.$refs.selectDigits.selected
            },
            ok: function () {
                this.readData();
                this.setConfig();
                this.toCalc();
            },
            cancel: function () {
                this.toCalc();
            },

            toCalc: function () {
                this.$root.$emit('toCalc');
            },
            setConfig: function () {
               const confAddress = 'http://127.0.0.1:' + this.$root.port + '/Configure';
               const headers = {
                 'Access-Control-Allow-Origin': confAddress
               };
               Vue.axios.post(confAddress, this.$data, { headers })
                .catch(e => {
                  this.errors.push(e.message)
                })
            },
        }
});