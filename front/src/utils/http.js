import axios from 'axios'
import router from '@/router/router-static'
import storage from '@/utils/storage'
import { Message } from 'element-ui'

const http = axios.create({
    timeout: 1000 * 86400,
    withCredentials: true,
    baseURL: '/springbootrd362',
    headers: {
        'Content-Type': 'application/json; charset=utf-8'
    }
})
// 请求拦截
http.interceptors.request.use(config => {
    config.headers['Token'] = storage.get('Token') // 请求头带上token
    return config
}, error => {
    return Promise.reject(error)
})
// 响应拦截
http.interceptors.response.use(response => {
    if (response.data && response.data.code === 401) { // 401, token失效
        Message.error('登录已过期，请重新登录')
        router.push({ name: 'login' })
    }
    return response
}, error => {
    if (error.code === 'ECONNABORTED' || error.message.includes('timeout')) {
        Message({ message: '请求超时，请检查网络后重试', type: 'error', duration: 4000 })
    } else if (!error.response) {
        Message({ message: '网络连接异常，请检查网络设置', type: 'error', duration: 4000 })
    } else {
        const status = error.response.status
        const msgMap = {
            400: '请求参数错误',
            403: '没有权限访问该资源',
            404: '请求的资源不存在',
            500: '服务器内部错误，请稍后重试',
            502: '网关错误，服务暂不可用',
            503: '服务暂时不可用，请稍后重试'
        }
        Message({ message: msgMap[status] || `请求异常（${status}）`, type: 'error', duration: 4000 })
    }
    return Promise.reject(error)
})
export default http