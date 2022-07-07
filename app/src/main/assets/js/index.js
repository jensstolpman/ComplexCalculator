Vue.axios=axios;
new Vue({
    el: '#calculator',
    data: {
        value: 0,
        command: '',
        logs: [],
        errors: [],
        port: 8080,
    },
    newEnter: 0,
    mounted:function(){
       this.setPort(this.getUrlParam('port'));
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
        calculate: function (e) {
           this.newEnter = 1;
           this.command=e;
           var address = 'http://127.0.0.1:' + this.port + '/Calculate';
           const headers = {
               'Access-Control-Allow-Origin': address
           };
           Vue.axios.post(address, this.$data, { headers })
            .then(response => { this.value = response.data.value})
            .catch(e => {
              this.errors.push(e.message)
            })
        },
        getUrlParam: function getUrlParam(name) {
          var url_string = window.location;
          var url = new URL(url_string);
          var c = url.searchParams.get(name);
          return c;
        },
        setPort: function setPort(port){
            this.port=port;
        }
    }
});
