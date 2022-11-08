import Vue from 'vue'
import Vuex from 'vuex'
import getters from './getters'
import app from './modules/app'
import settings from './modules/settings'
import user from './modules/user'
import survey from './modules/survey'
import permission from '@/store/modules/permission'

import createPersistedState from 'vuex-persistedstate' // vuex持久化
Vue.use(Vuex)
const store = new Vuex.Store({
  modules: {
    app,
    settings,
    user,
    survey,
    permission
  },
  plugins: [createPersistedState()], // vuex持久化
  getters
})

export default store
