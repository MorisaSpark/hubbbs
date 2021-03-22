import Vue from 'vue'
import Router from 'vue-router'

// in development-env not use lazy-loading, because lazy-loading too many pages will cause webpack hot update too slow. so only in production use lazy-loading;
// detail: https://panjiachen.github.io/vue-element-admin-site/#/lazy-loading

Vue.use(Router)

/* Layout */
import Layout from '../views/layout/Layout'

/**
* hidden: true                   if `hidden:true` will not show in the sidebar(default is false)
* alwaysShow: true               if set true, will always show the root menu, whatever its child routes length
*                                if not set alwaysShow, only more than one route under the children
*                                it will becomes nested mode, otherwise not show the root menu
* redirect: noredirect           if `redirect:noredirect` will no redirct in the breadcrumb
* name:'router-name'             the name is used by <keep-alive> (must set!!!)
* meta : {
    title: 'title'               the name show in submenu and breadcrumb (recommend set)
    icon: 'svg-name'             the icon show in the sidebar,
  }
**/
export const constantRouterMap = [
  { path: '/login', component: () => import('@/views/login/index'), hidden: true },
  { path: '/404', component: () => import('@/views/404'), hidden: true },
  {
    path: '/',
    component: Layout,
    redirect: '/dashboard',
    name: 'Dashboard',
    hidden: true,
    children: [{
      path: 'dashboard',
      component: () => import('@/views/dashboard/index')
    }]
  },
  {
    path: '/example',
    component: Layout,
    redirect: '/example/table',
    name: 'Example',
    meta: { title: '基本信息管理', icon: 'example' },
    children: [
      { path: 'account', name: 'account', component: () => import('@/views/table/account'), meta: { title: '账号管理', icon: 'table' }},
      { path: 'attenter', name: 'attenter', component: () => import('@/views/table/attenter'), meta: { title: '关注管理', icon: 'table' }},
      { path: 'ban', name: 'ban', component: () => import('@/views/table/ban'), meta: { title: '封禁管理', icon: 'table' }},
      { path: 'cookie', name: 'cookie', component: () => import('@/views/table/cookie'), meta: { title: '饼干管理', icon: 'table' }},
      { path: 'identity', name: 'identity', component: () => import('@/views/table/identity'), meta: { title: '认证管理', icon: 'table' }},
      { path: 'message', name: 'message', component: () => import('@/views/table/message'), meta: { title: '私信管理', icon: 'table' }},
      { path: 'post', name: 'post', component: () => import('@/views/table/post'), meta: { title: '文章管理', icon: 'table' }},
      { path: 'relcollection', name: 'relcollection', component: () => import('@/views/table/relcollection'), meta: { title: '收藏管理', icon: 'table' }},
      { path: 'reltag', name: 'reltag', component: () => import('@/views/table/reltag'), meta: { title: '标签管理', icon: 'table' }},
      { path: 'reply', name: 'reply', component: () => import('@/views/table/reply'), meta: { title: '评论管理', icon: 'table' }},
      { path: 'report', name: 'report', component: () => import('@/views/table/report'), meta: { title: '举报管理', icon: 'table' }},
      { path: 'type', name: 'type', component: () => import('@/views/table/type'), meta: { title: '类型管理', icon: 'table' }},
      { path: 'user', name: 'user', component: () => import('@/views/table/user'), meta: { title: '用户管理', icon: 'table' }}
    ]
  },
  { path: '*', redirect: '/404', hidden: true }
]

export default new Router({
  // mode: 'history', //后端支持可开
  scrollBehavior: () => ({ y: 0 }),
  routes: constantRouterMap
})

