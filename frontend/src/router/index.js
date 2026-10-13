import Vue from 'vue'
import VueRouter from 'vue-router'
import store from '../store'
import routes from "@/router/routes"
import NProgress from 'nprogress'
import 'nprogress/nprogress.css'

//顶部进度条样式
NProgress.configure({
    showSpinner: false,
    speed: 800,
});

Vue.use(VueRouter)

const router = new VueRouter({
    mode: 'history',
    base: process.env.BASE_URL,
    routes
})

// 检查用户是否有访问某个路径的权限
function hasPermission(userRoles, path) {
    // 定义路径与角色的映射
    const pathRoleMap = {
        '/commodity': ['ROLE_COMMODITY', 'ROLE_SUPER_ADMIN'],
        '/company': ['ROLE_SALE', 'ROLE_SUPER_ADMIN'],
        '/employee': ['ROLE_SUPER_ADMIN'],
        '/warehouse': ['ROLE_WAREHOUSE', 'ROLE_SUPER_ADMIN'],
        '/warehouse/': ['ROLE_WAREHOUSE', 'ROLE_ADMIN', 'ROLE_SUPER_ADMIN'],
        '/sale/create': ['ROLE_SUPER_ADMIN'],
        '/sale/record': ['ROLE_SALE', 'ROLE_SUPER_ADMIN'],
        '/delivery/create': ['ROLE_SALE', 'ROLE_EMPLOYEE', 'ROLE_COMMODITY', 'ROLE_WAREHOUSE', 'ROLE_SUPER_ADMIN'],
        '/delivery/list': ['ROLE_SALE', 'ROLE_EMPLOYEE', 'ROLE_COMMODITY', 'ROLE_WAREHOUSE', 'ROLE_SUPER_ADMIN'],
        '/vehicle': ['ROLE_SALE', 'ROLE_EMPLOYEE', 'ROLE_COMMODITY', 'ROLE_WAREHOUSE', 'ROLE_SUPER_ADMIN'],
        '/driver': ['ROLE_SALE', 'ROLE_EMPLOYEE', 'ROLE_COMMODITY', 'ROLE_WAREHOUSE', 'ROLE_SUPER_ADMIN'],
        '/analyze/in': ['ROLE_SALE', 'ROLE_EMPLOYEE', 'ROLE_COMMODITY', 'ROLE_WAREHOUSE', 'ROLE_SUPER_ADMIN'],
        '/analyze/out': ['ROLE_SALE', 'ROLE_EMPLOYEE', 'ROLE_COMMODITY', 'ROLE_WAREHOUSE', 'ROLE_SUPER_ADMIN'],
        '/security': ['ROLE_WAREHOUSE', 'ROLE_SALE', 'ROLE_EMPLOYEE', 'ROLE_COMMODITY', 'ROLE_SUPER_ADMIN','ROLE_COURIER','ROLE_USER'],
        '/admin': ['ROLE_SUPER_ADMIN'],
        '/role': ['ROLE_SUPER_ADMIN'],
        '/loginlog': ['ROLE_WAREHOUSE', 'ROLE_SALE', 'ROLE_EMPLOYEE', 'ROLE_COMMODITY', 'ROLE_SUPER_ADMIN'],
        '/systemlog': ['ROLE_WAREHOUSE', 'ROLE_SALE', 'ROLE_EMPLOYEE', 'ROLE_COMMODITY', 'ROLE_SUPER_ADMIN'],
        '/inventory/in': ['ROLE_WAREHOUSE', 'ROLE_SUPER_ADMIN'],
        '/inventory/out': ['ROLE_WAREHOUSE', 'ROLE_SUPER_ADMIN'],
        '/express/list': ['ROLE_COURIER', 'ROLE_SUPER_ADMIN','ROLE_USER'],
        '/express/in': ['ROLE_COURIER', 'ROLE_SUPER_ADMIN','ROLE_USER'],
        '/express/out': ['ROLE_COURIER', 'ROLE_SUPER_ADMIN','ROLE_USER'],
        '/express/stock': ['ROLE_SUPER_ADMIN'],
        '/cabinet/box:cabinetId': ['ROLE_SUPER_ADMIN'],
        '/cabinet/list': ['ROLE_SUPER_ADMIN'],
        '/cabinet/box': ['ROLE_SUPER_ADMIN'],
        '/express/feedback': ['ROLE_SUPER_ADMIN','ROLE_USER'],
        '/notice':['ROLE_COURIER', 'ROLE_SUPER_ADMIN','ROLE_USER']
    };

    // 错误页面、登录页等不需要特定权限
    const publicPaths = ['/login', '/init', '/403', '/404', '/500'];

    // 公共页面无需权限即可访问
    if (publicPaths.includes(path)) {
        return true;
    }

    // 获取路径对应的所需角色
    let requiredRoles = null;

    // 处理嵌套路径，如 /warehouse/123
    for (const key in pathRoleMap) {
        if (path.startsWith(key)) {
            requiredRoles = pathRoleMap[key];
            break;
        }
    }

    // 如果没有找到路径对应的角色要求，默认视为无权限
    if (!requiredRoles) {
        return false;
    }

    // 检查用户是否有需要的角色
    for (const role of userRoles) {
        if (requiredRoles.includes(role)) {
            return true;
        }
    }

    return false;
}

//路由卫士
router.beforeEach((to, from, next) => {
    NProgress.start();

    // 如果不需要登录认证，直接放行
    if (!to.meta.auth) {
        next();
        return;
    }

    // 如果未登录且需要认证，则重定向到登录页
    if (!store.state.user.token) {
        next('/login');
        return;
    }

    // 已登录，检查是否有访问权限
    const userDetails = store.state.user.details;
    if (userDetails && userDetails.roles) {
        const userRoles = userDetails.roles.split(';');

        // 检查用户是否有权限访问该路径
        if (hasPermission(userRoles, to.path)) {
            next(); // 有权限，允许访问
        } else {
            next('/403'); // 无权限，重定向到403页面
        }
    } else {
        next('/login'); // 如果用户信息不完整，重定向到登录页
    }
});

router.afterEach(() => {
    NProgress.done()
})

export default router
