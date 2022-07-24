document.write(`
    <script type="text/x-template" id = "menu-template">
      <div id = "calc-menu">
        <button class="menu-btn" @click="toCalc()">Calc</button>
        <button class="menu-btn"> </button>
        <button class="menu-btn" @click="toConf()">Conf</button>
      </div>
    </script>

    <style>
        #calc-menu .menu-btn {
            color:#D4D4D2;
            background-color: #2f2f31;
            border: none;
            font-size: 3vh;
        }
    </style>
`);

Vue.component('calc-menu', {
        template: '#menu-template',
        data() {
            return {
                decimals: 4,
            }
        },
        mounted() {
                this.$root.$on('toCalc', () => {
                    this.toCalc()
                });
        },
        methods: {
            toCalc: function() {
                this.$root.currentRoute = 'calc';
            },
            toConf: function() {
                this.$root.currentRoute = 'config';
            },
        }
});