package cn.mdmbct.seckill.common.filter;

import cn.mdmbct.seckill.common.Participant;

/**
 * 参与次数过滤器
 *
 * @author mdmbct  mdmbct@outlook.com
 * @date 2021/11/21 9:34
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
    public void doFilter(Participant participant, FilterRes res) {
        if (getCurParticipationNum() >= participationNum) {
            res.setFilterNotPassed(this);
            return;
        }
        doNextFilter(participant, res, this);
    }

    @Override
    public String notPassMsg() {
        return "参与次数过多！";
    }

    private int getCurParticipationNum() {
        return 0;
    }

}
