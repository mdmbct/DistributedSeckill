package cn.mdmbct.seckill.common.limiter;

import cn.mdmbct.seckill.common.Participant;

/**
 * 参与次数限制
 *
 * @author mdmbct  mdmbct@outlook.com
 * @date 2021/11/21 9:34
 * @modified mdmbct
 * @since 0.1
 */
public class ParticipationNumLimiter extends BaseLimiter {


    private final int participationNum;


    public ParticipationNumLimiter(int order, int participationNum) {
        super(order);
        this.participationNum = participationNum;
    }

    @Override
    public void doLimit(Participant participant, LimitContext context) {
        if (getCurParticipationNum() >= participationNum) {
            context.setLimiterNotPassed(this);
            return;
        }
        doNextLimit(participant, context);
    }

    @Override
    public String notPassMsg() {
        return "参与次数过多！";
    }

    private int getCurParticipationNum() {
        return 0;
    }

}
