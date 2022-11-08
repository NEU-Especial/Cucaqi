import router from './router'
import store from './store'
import NProgress from 'nprogress' // progress bar
import 'nprogress/nprogress.css' // progress bar style
import getPageTitle from '@/utils/get-page-title'

NProgress.configure({ showSpinner: false }) // NProgress Configuration

router.beforeEach(async(to, from, next) => {
  NProgress.start()
  document.title = getPageTitle(to.meta.title)
  if (to.path.includes('/login')) {
    next()
    NProgress.done()
  } else {
    const hasRoles = store.getters.permission_routes && store.getters.permission_routes.length > 0// 判断是否已经生成过了
    if (hasRoles) {
      next()
    } else {
      const roles = store.getters.roles
      const accessRoutes = await store.dispatch('permission/generateRoutes', roles)
      router.addRoutes(accessRoutes)
      next({ ...to, replace: true })
    }
  }
})

router.afterEach(() => {
  // finish progress bar
  NProgress.done()
})
