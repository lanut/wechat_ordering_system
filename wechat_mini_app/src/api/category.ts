import type { CategoryItem } from '@/types/category'
import { http } from '@/utils/http'

/**
 * 分类列表-小程序
 */
export const getCategoryAPI = () => {
  return http<CategoryItem[]>({
    // todo 少一个type参数
    method: 'GET',
    url: '/user/category/list'
  })
}
