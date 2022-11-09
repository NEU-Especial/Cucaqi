import Vue from 'vue'
import Router from 'vue-router'
/* Layout */
import Layout from '@/layout'

Vue.use(Router)

/**
 * Note: sub-menu only appear when route children.length >= 1
 *
 * hidden: true                   if set true, item will not show in the sidebar(default is false)
 * alwaysShow: true               if set true, will always show the root menu
 *                                if not set alwaysShow, when item has more than one children route,
 *                                it will becomes nested mode, otherwise not show the root menu
 * redirect: noRedirect           if set noRedirect will no redirect in the breadcrumb
 * name:'router-name'             the name is used by <keep-alive> (must set!!!)
 * meta : {
    roles: ['admin','editor']    control the page roles (you can set multiple roles)
    title: 'title'               the name show in sidebar and breadcrumb (recommend set)
    icon: 'svg-name'/'el-icon-x' the icon show in the sidebar
    breadcrumb: false            if set false, the item will hidden in breadcrumb(default is true)
    activeMenu: '/example/list'  if set path, the sidebar will highlight the path you set
  }
 */

export const asyncRoutes = [
  {
    path: '/login',
    component: () => import('@/views/login/index'),
    hidden: true,
    children: [
      {
        path: '',
        name: 'Login',
        component: () => import('@/views/login/password')
      },
      {
        // 注册界面
        path: 'register',
        name: 'Register',
        component: () => import('@/views/login/register')
      },
      {
        // 电话号码登陆
        path: 'telephone',
        name: 'Telephone',
        component: () => import('@/views/login/telephone')
      },
      {
        // 密保或者手机号找回
        path: 'findBack',
        name: 'findBack',
        component: () => import('@/views/login/findback')
      }
    ]
  },

  {
    path: '/404',
    component: () => import('@/views/404'),
    hidden: true
  },

  {
    path: '/',
    component: Layout,
    redirect: '/dashboard',
    children: [{
      path: 'dashboard',
      name: 'Dashboard',
      component: () => import('@/views/dashboard/index'),
      meta: { title: '数据大屏', icon: 'dashboard', roles: [1001, 1003] }
    },
    {
      path: 'profile',
      name: 'Profile',
      component: () => import('@/views/profile/profile'),
      hidden: true,
      meta: { title: '用户信息', icon: 'dashboard' }
    }
    ]
  },
  {
    path: '/questionnaire',
    component: Layout,
    redirect: '/questionnaire/list',
    name: 'Questionnaire',
    meta: { title: '问卷', icon: 'el-icon-s-help', roles: [1003] },
    children: [
      {
        path: 'list',
        name: 'List',
        component: () => import('@/views/questionnaire/list'),
        meta: { title: '问卷管理', icon: 'table' }
      },
      {
        path: 'create',
        name: 'Create',
        component: () => import('@/views/questionnaire/create'),
        meta: { title: '创建问卷', icon: 'table' }
      },
      {
        path: 'preview',
        name: 'Preview',
        component: () => import('@/views/questionnaire/preview'),
        hidden: true,
        meta: { title: '模拟答题', icon: 'tree' }
      },
      {
        path: 'post',
        name: 'Post',
        component: () => import('@/views/questionnaire/post'),
        hidden: true,
        meta: { title: '发布问卷', icon: 'tree' }
      },
      {
        path: 'edit',
        name: 'Edit',
        component: () => import('@/views/questionnaire/editor'),
        hidden: true,
        meta: { title: '编辑问卷', icon: 'tree' }
      }
    ]
  },
  {
    path: '/user',
    component: Layout,
    meta: { title: '用户管理', icon: 'el-icon-s-help', roles: [1002] },
    children: [
      {
        path: '',
        name: 'User',
        component: () => import('@/views/user/user'),
        meta: { title: '用户管理', icon: 'table', roles: [1002] }
      }
    ]
  },
  {
    path: '/lessee',
    component: Layout,
    children: [
      {
        path: 'index',
        name: 'Control',
        component: () => import('@/views/lessee/lessee'),
        meta: { title: '租户管理', icon: 'form', roles: [1001] }
      }
    ]

  },

  {
    path: '/group',
    component: Layout,
    meta: { title: '群组', icon: 'el-icon-s-help', roles: [1003] },
    children: [
      {
        path: 'controller',
        name: 'Controller',
        component: () => import('@/views/group/group'),
        meta: { title: '群组管理', icon: 'form', roles: [1003] }
      },
      {
        path: 'details',
        name: 'Details',
        hidden: true,
        component: () => import('@/views/group/details/groupDetails'),
        meta: { title: '群组详情', icon: 'form', roles: [1003] }
      }
    ]
  },

  {
    path: '/answerer',
    component: Layout,
    children: [
      {
        path: 'controller',
        name: 'Answerer',
        component: () => import('@/views/answerer/answerer'),
        meta: { title: '答者管理', icon: 'form', roles: [1003] }
      },
      {
        path: 'belongs',
        name: 'Belongs',
        hidden: true,
        component: () => import('@/views/answerer/belongs/belongGroup'),
        meta: { title: '所属群组', icon: 'form', roles: [1003] }
      }
    ]
  },

  {
    path: 'external-link',
    component: Layout,
    children: [
      {
        path: 'https://github.com/NEU-Especial/Cucaqi',
        meta: { title: '代码仓库', icon: 'link' }
      }
    ]
  },

  // 404 page must be placed at the end !!!
  { path: '*', redirect: '/404', hidden: true }
]

const createRouter = () => new Router({
  mode: 'history', // require service support
  scrollBehavior: () => ({ y: 0 }),
  routes: asyncRoutes
})

const router = createRouter()

// Detail see: https://github.com/vuejs/vue-router/issues/1234#issuecomment-357941465
export function resetRouter() {
  const newRouter = createRouter()
  router.matcher = newRouter.matcher // reset router
}

export default router
