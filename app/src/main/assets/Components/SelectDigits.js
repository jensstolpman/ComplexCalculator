document.write(`
    <script type="text/x-template" id = "select-digits-template">
      <div id = "select-digits">
          <div class="custom-select" :tabindex="tabindex" @blur="open = false">
            <div class="selected" :class="{ open: open }" @click="open = !open">
              {{ selected }}
            </div>
            <div class="items" :class="{ selectHide: !open }">
              <div
                v-for="(option, i) of options"
                :key="i"
                @click="
                  selected = option;
                  open = false;
                  $emit('input', option);
                "
              >
                {{ option }}
              </div>
            </div>
          </div>
      </div>

    </script>

    <style>
        .custom-select {
          position: relative;
          width: 130%;
          text-align: left;
          outline: none;
          background-color: #000000;
          border: none;
          height: 5vh;
        }

        .custom-select .selected {
          border-radius: 6px;
          color: #fff;
          padding-left: 1em;
          cursor: pointer;
          user-select: none;
          height: 5vh;
        }

        .custom-select .selected.open {
          border-radius: 6px 6px 0px 0px;
        }

        .custom-select .selected:after {
          position: absolute;
          content: "";
          top: 2.5vh;
          right: 1vh;
          width: 0;
          height: 0;
          border: 0.5vh solid transparent;
          border-color: #fff transparent transparent transparent;
        }

        .custom-select .items {
          color: #fff;
          border-radius: 0px 0px 6px 6px;
          overflow: hidden;
          position: absolute;
          background-color: #0a0a0a;
          left: 0;
          right: 0;
          z-index: 1;
        }

        .custom-select .items div {
          color: #fff;
          padding-left: 1em;
          cursor: pointer;
          user-select: none;
        }

        .custom-select .items div:hover {
          background-color: #ad8225;
        }

        .selectHide {
          display: none;
        }

    </style>

`);

Vue.component('select-digits', {
      template: '#select-digits-template',
      props: {
        options: {
          type: Array,
          required: true,
        },
        default: {
          type: Number,
          required: false,
          default: null,
        },
        tabindex: {
          type: Number,
          required: false,
          default: 0,
        },
      },
      data() {
        return {
          selected: this.default
            ? this.default
            : this.options.length > 0
            ? this.options[0]
            : null,
          open: false,
        };
      },
      mounted() {
        this.$emit("input", this.selected);
      },
});