package cn.mdmbct.seckill.common.filter;

import cn.mdmbct.seckill.common.Participant;
import cn.mdmbct.seckill.common.context.FilterContext;
import cn.mdmbct.seckill.common.lock.CompeteLockRes;
import cn.mdmbct.seckill.common.repository.ProductsRepository;

/**
 * 奖励过滤器，最后一个过滤器 <br>
 * 得到锁之后，操作仓库数量减一 <br>
 * 只能通过从仓库中拿到奖励的线程 <br>
 * 注：奖励可以是抽奖后的奖品、秒杀到的商品、抢到的红包
 *
 * @author mdmbct  mdmbct@outlook.com
 * @date 2021/11/22 8:22
 * @modified mdmbct
 * @since 0.1
 */
public class AwardFilter extends BaseFilter {

    private final ProductsRepository productsRepository;

    public AwardFilter(ProductsRepository productsRepository) {
        super(LAST_FILTER_ORDER);
        this.productsRepository = productsRepository;
    }

    @Override
    public String notPassMsg() {
        return null;
    }

    @Override
    public void doFilter(Participant participant, String productId) {
        // 修改库存 并释放锁

        final FilterContext filterContext = getFilterContext();
        final CompeteLockRes competeLockRes = productsRepository.decrOne(productId);
        filterContext.getCompetedLock().unLock(productId);

    }
}
