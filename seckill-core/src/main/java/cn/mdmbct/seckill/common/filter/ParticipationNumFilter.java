package cn.mdmbct.seckill.common.filter;

import cn.mdmbct.seckill.common.Participant;

/**
 * @author mdmbct  mdmbct@outlook.com
 * @date 2021/11/22 8:17
 * @modified mdmbct
 * @since 0.1
 */
public class ParticipationNumFilter extends BaseFilter {

    private final int participationNum;

    public ParticipationNumFilter(int order, int participationNum) {
        super(order);
        this.participationNum = participationNum;
    }

    @Override
    public String notPassMsg() {
        return "参与次数过多！";
    }

    @Override
    public void doFilter(Participant participant, String productId) {
        if (getCurParticipationNum() >= participationNum) {
            getFilterContext().setFilterNotPassed(this);
            return;
        }
        doNextFilter(participant, productId);
    }

    private int getCurParticipationNum() {
        return 0;
    }

}
