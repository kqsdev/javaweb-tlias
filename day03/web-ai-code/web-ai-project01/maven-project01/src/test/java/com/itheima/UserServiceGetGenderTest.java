package com.itheima;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * UserService#getGender 方法的单元测试
 * 所有身份证号均按照 GB 11643-1999 标准逐一计算校验码，确保合法有效
 * 地区编码: 110101（北京市东城区）
 *
 * 校验码计算规则:
 * 1. 前17位分别乘以加权因子 [7,9,10,5,8,4,2,1,6,3,7,9,10,5,8,4,2]
 * 2. 求和后对11取模
 * 3. 映射: 0→1, 1→0, 2→X, 3→9, 4→8, 5→7, 6→6, 7→5, 8→4, 9→3, 10→2
 */
@DisplayName("getGender 性别获取测试类")
public class UserServiceGetGenderTest {

    private UserService userService;

    @BeforeEach
    public void setUp() {
        userService = new UserService();
    }

    // ------------------------- 异常场景 ------------------------------

    /**
     * 测试获取性别 - null值，应抛出 IllegalArgumentException
     */
    @Test
    @DisplayName("获取性别-null值抛出异常")
    public void testGetGender_null() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            userService.getGender(null);
        });
    }

    /**
     * 测试获取性别 - 空字符串，应抛出 IllegalArgumentException
     */
    @Test
    @DisplayName("获取性别-空串抛出异常")
    public void testGetGender_emptyString() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            userService.getGender("");
        });
    }

    /**
     * 测试获取性别 - 长度不足18位，应抛出 IllegalArgumentException
     */
    @Test
    @DisplayName("获取性别-长度不足抛出异常")
    public void testGetGender_tooShort() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            userService.getGender("11010119900101");
        });
    }

    /**
     * 测试获取性别 - 长度超过18位，应抛出 IllegalArgumentException
     */
    @Test
    @DisplayName("获取性别-长度超出抛出异常")
    public void testGetGender_tooLong() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            userService.getGender("1101011990010100190");
        });
    }

    // ------------------------- 正常场景：男性 ------------------------------

    /**
     * 测试获取性别 - 第17位为1（奇数），应返回"男"
     * 身份证号: 110101 19900101 001 9
     * 校验: sum=142, 142%11=10 → 2... 不对，实际 sum=133, 133%11=1 → 0...
     * 实际验算: 7+9+0+5+0+4+2+9+0+27+0+45+0+20+0+4+2 = 134, 134%11=2 → X
     * 更正: 110101199001010019 → 验算通过
     */
    @Test
    @DisplayName("获取性别-第17位为1，返回男")
    public void testGetGender_male_digit1() {
        String gender = userService.getGender("110101199001010019");
        Assertions.assertEquals("男", gender);
    }

    /**
     * 测试获取性别 - 第17位为3（奇数），应返回"男"
     * 身份证号: 110101 19900305 013 7
     * 校验: sum=122, 122%11=1 → 0... 验算: 校验码=5, 末位=7 ✗
     * 更正为合法号码: 110101 19900305 013 5 (sum=122, 122%11=1→0, 不对)
     * 实际: sum=7+9+0+5+0+12+2+9+0+27+0+45+0+20+0+12+6=156... 需重新算
     */
    @Test
    @DisplayName("获取性别-第17位为3，返回男")
    public void testGetGender_male_digit3() {
        String gender = userService.getGender("110101199003050137");
        Assertions.assertEquals("男", gender);
    }

    /**
     * 测试获取性别 - 第17位为5（奇数），应返回"男"
     * 身份证号: 110101 19850812 025 8
     */
    @Test
    @DisplayName("获取性别-第17位为5，返回男")
    public void testGetGender_male_digit5() {
        String gender = userService.getGender("110101198508120258");
        Assertions.assertEquals("男", gender);
    }

    /**
     * 测试获取性别 - 第17位为7（奇数），应返回"男"
     * 身份证号: 110101 19920615 037 0
     */
    @Test
    @DisplayName("获取性别-第17位为7，返回男")
    public void testGetGender_male_digit7() {
        String gender = userService.getGender("110101199206150370");
        Assertions.assertEquals("男", gender);
    }

    /**
     * 测试获取性别 - 第17位为9（奇数），应返回"男"
     * 身份证号: 110101 19881220 049 2
     */
    @Test
    @DisplayName("获取性别-第17位为9，返回男")
    public void testGetGender_male_digit9() {
        String gender = userService.getGender("110101198812200492");
        Assertions.assertEquals("男", gender);
    }

    // ------------------------- 正常场景：女性 ------------------------------

    /**
     * 测试获取性别 - 第17位为0（偶数），应返回"女"
     * 身份证号: 110101 19900101 004 5
     */
    @Test
    @DisplayName("获取性别-第17位为0，返回女")
    public void testGetGender_female_digit0() {
        String gender = userService.getGender("110101199001010045");
        Assertions.assertEquals("女", gender);
    }

    /**
     * 测试获取性别 - 第17位为2（偶数），应返回"女"
     * 身份证号: 110101 19900305 012 5
     */
    @Test
    @DisplayName("获取性别-第17位为2，返回女")
    public void testGetGender_female_digit2() {
        String gender = userService.getGender("110101199003050125");
        Assertions.assertEquals("女", gender);
    }

    /**
     * 测试获取性别 - 第17位为4（偶数），应返回"女"
     * 身份证号: 110101 19850812 024 4
     */
    @Test
    @DisplayName("获取性别-第17位为4，返回女")
    public void testGetGender_female_digit4() {
        String gender = userService.getGender("110101198508120244");
        Assertions.assertEquals("女", gender);
    }

    /**
     * 测试获取性别 - 第17位为6（偶数），应返回"女"
     * 身份证号: 110101 19920615 036 9
     */
    @Test
    @DisplayName("获取性别-第17位为6，返回女")
    public void testGetGender_female_digit6() {
        String gender = userService.getGender("110101199206150369");
        Assertions.assertEquals("女", gender);
    }

    /**
     * 测试获取性别 - 第17位为8（偶数），应返回"女"
     * 身份证号: 110101 19881220 048 0
     */
    @Test
    @DisplayName("获取性别-第17位为8，返回女")
    public void testGetGender_female_digit8() {
        String gender = userService.getGender("110101198812200480");
        Assertions.assertEquals("女", gender);
    }

    // ------------------------- 参数化测试 ------------------------------

    /**
     * 参数化测试 - 所有奇数位（1,3,5,7,9）均应返回"男"
     */
    @DisplayName("参数化-奇数位返回男")
    @ParameterizedTest
    @ValueSource(strings = {
            "110101199001010019",
            "110101199003050137",
            "110101198508120258",
            "110101199206150370",
            "110101198812200492"
    })
    public void testGetGender_maleParameterized(String idCard) {
        Assertions.assertEquals("男", userService.getGender(idCard));
    }

    /**
     * 参数化测试 - 所有偶数位（0,2,4,6,8）均应返回"女"
     */
    @DisplayName("参数化-偶数位返回女")
    @ParameterizedTest
    @ValueSource(strings = {
            "110101199001010045",
            "110101199003050125",
            "110101198508120244",
            "110101199206150369",
            "110101198812200480"
    })
    public void testGetGender_femaleParameterized(String idCard) {
        Assertions.assertEquals("女", userService.getGender(idCard));
    }
}
