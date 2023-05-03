package com.example.testchart.util;

public class SC300LIB {
    public SC300LIB() {
    }

    public int sc300_get_trajectory(double speed, double angle, int spin, int temperature, int pressure, double carry, double apex, double[] trajectory_t, double[] trajectory_y, double[] trajectory_z) {
        double dt = 0.005D;
        double rho = (double)pressure / (2.87058D * (273.15D + (double)temperature));
        double K = -rho * 0.0013267D / (2.0D * 0.046D); // 0.046: mass of ball
        double rad = angle * 3.141592D / 180.0D;
        double[] output_t = new double[3200];
        double[] output_y = new double[3200];
        double[] output_z = new double[3200];
        double cos_alpha = Math.cos(rad);
        double sin_alpha = Math.sin(rad);
        double pyc = 0.0D;
        double pzc = 0.0D;
        double pyn = 0.0D;
        double pzn = 0.0D;
        double vyc = 0.0D;
        double vzc = 0.0D;
        double vyn = speed * cos_alpha;
        double vzn = speed * sin_alpha;
        int n = 0;
        double zmax0 = 0.0D;
        double zmax = 0.0D;
        int zmax_index = 0;
        output_t[0] = 0.0D;
        output_y[0] = 0.0D;
        output_z[0] = 0.0D;

        double r;
        for(int i = 0; i < 3200; ++i) {
            pyc = pyn;
            pzc = pzn;
            vyc = vyn;
            vzc = vzn;
            double v2 = vyn * vyn + vzn * vzn;
            double v1 = Math.sqrt(v2);
            double v3 = v2 * v1;
            double v4 = v2 * v2;
            double v5 = v3 * v2;
            double v6 = v3 * v3;
            double CD;
            double CL;
            if (spin % 250 == 0) {
                CD = this.KHG_CD(v1, v2, v3, v4, v5, v6, spin);
                CL = this.KHG_CL(v1, v2, v3, v4, v5, v6, spin);
            } else {
                int spl = spin - spin % 250;
                int sph = spl + 250;
                r = (double)(spin - spl) / 250.0D;
                double cdl = this.KHG_CD(v1, v2, v3, v4, v5, v6, spl);
                double cdh = this.KHG_CD(v1, v2, v3, v4, v5, v6, sph);
                double cll = this.KHG_CL(v1, v2, v3, v4, v5, v6, spl);
                double clh = this.KHG_CL(v1, v2, v3, v4, v5, v6, sph);
                CD = cdl + r * (cdh - cdl);
                CL = cll + r * (clh - cll);
            }

            if (v1 == 0.0D) {
                break;
            }

            cos_alpha = vyn / v1;
            sin_alpha = vzn / v1;
            double ayc = K * v2 * (CD * cos_alpha + CL * sin_alpha);
            double azc = K * v2 * (CD * sin_alpha - CL * cos_alpha) - 9.8D;
            vyn += ayc * dt;
            vzn += azc * dt;
            pyn = pyn + vyc * dt + 0.5D * ayc * dt * dt;
            pzn = pzn + vzc * dt + 0.5D * azc * dt * dt;
            output_t[i] = (double)i * dt;
            output_y[i] = pyc;
            output_z[i] = pzc;
            if (pzc > zmax0) {
                zmax0 = pzc;
            }

            if (pzc > 0.0D && pzn < 0.0D) {
                n = i;
                break;
            }
        }

        if (n >= 200 && output_y[n] > 0.0D) {
            r = carry > 0.0D ? carry / output_y[n] : 1.0D;

            int j;
            for(j = 0; j < 200; ++j) {
                int k = (int)((double)(n * j) / 199.0D);
                trajectory_t[j] = output_t[k];
                trajectory_y[j] = output_y[k] * r;
                trajectory_z[j] = output_z[k];
                if (trajectory_z[j] > zmax) {
                    zmax = trajectory_z[j];
                    zmax_index = j;
                }
            }

            if (zmax <= 0.0D) {
                return 0;
            } else {
                r = apex > 0.0D ? apex / zmax : zmax0 / zmax;

                for(j = 0; j < 200; ++j) {
                    trajectory_z[j] *= r;
                }

                return zmax_index;
            }
        } else {
            return 0;
        }
    }

    private double KHG_CD(double v1, double v2, double v3, double v4, double v5, double v6, int spin) {
        double CD;
        switch(spin) {
            case 500:
                CD = 0.804285D - 0.0623645D * v1 + 0.00252163D * v2 - 5.31858E-5D * v3 + 6.28812E-7D * v4 - 3.97155E-9D * v5 + 1.04349E-11D * v6;
                break;
            case 750:
                CD = 0.800285D - 0.0623645D * v1 + 0.00252163D * v2 - 5.31858E-5D * v3 + 6.28812E-7D * v4 - 3.97155E-9D * v5 + 1.04349E-11D * v6;
                break;
            case 1000:
                CD = 0.783984D - 0.0570641D * v1 + 0.00227902D * v2 - 4.79969E-5D * v3 + 5.67668E-7D * v4 - 3.57777E-9D * v5 + 9.33951E-12D * v6;
                break;
            case 1250:
                CD = 0.767686D - 0.0517646D * v1 + 0.00203649D * v2 - 4.28111E-5D * v3 + 5.06582E-7D * v4 - 3.18449E-9D * v5 + 8.24578E-12D * v6;
                break;
            case 1500:
                CD = 0.741409D - 0.0464678D * v1 + 0.0017941D * v2 - 3.76287E-5D * v3 + 4.45542E-7D * v4 - 2.79152E-9D * v5 + 7.15285E-12D * v6;
                break;
            case 1750:
                CD = 0.714144D - 0.041174D * v1 + 0.00155193D * v2 - 3.24531E-5D * v3 + 3.84604E-7D * v4 - 2.39929E-9D * v5 + 6.06196E-12D * v6;
                break;
            case 2000:
                CD = 0.686724D - 0.0358586D * v1 + 0.00130857D * v2 - 2.72439E-5D * v3 + 3.23165E-7D * v4 - 2.00328E-9D * v5 + 4.95979E-12D * v6;
                break;
            case 2250:
                CD = 0.659392D - 0.0305555D * v1 + 0.00106592D * v2 - 2.20558E-5D * v3 + 2.6206E-7D * v4 - 1.60994E-9D * v5 + 3.86594E-12D * v6;
                break;
            case 2500:
                CD = 0.631914D - 0.0252326D * v1 + 8.22209E-4D * v2 - 1.68395E-5D * v3 + 2.00551E-7D * v4 - 1.21366E-9D * v5 + 2.76362E-12D * v6;
                break;
            case 2750:
                CD = 0.604656D - 0.0199394D * v1 + 5.80044E-4D * v2 - 1.16629E-5D * v3 + 1.39584E-7D * v4 - 8.21123E-10D * v5 + 1.67162E-12D * v6;
                break;
            case 3000:
                CD = 0.577136D - 0.0146099D * v1 + 3.35949E-4D * v2 - 6.43572E-6D * v3 + 7.79201E-8D * v4 - 4.23735E-10D * v5 + 5.66163E-13D * v6;
                break;
            case 3250:
                CD = 0.581602D - 0.0143864D * v1 + 3.2002E-4D * v2 - 5.92436E-6D * v3 + 7.0452E-8D * v4 - 3.74831E-10D * v5 + 4.55193E-13D * v6;
                break;
            case 3500:
                CD = 0.585866D - 0.0141351D * v1 + 3.02624E-4D * v2 - 5.37454E-6D * v3 + 6.24537E-8D * v4 - 3.22251E-10D * v5 + 3.34138E-13D * v6;
                break;
            case 3750:
                CD = 0.59029D - 0.0139049D * v1 + 2.86308E-4D * v2 - 4.85239E-6D * v3 + 5.48302E-8D * v4 - 2.72235E-10D * v5 + 2.20038E-13D * v6;
                break;
            case 4000:
                CD = 0.594643D - 0.0136664D * v1 + 2.69603E-4D * v2 - 4.32092E-6D * v3 + 4.70839E-8D * v4 - 2.21373E-10D * v5 + 1.03568E-13D * v6;
                break;
            case 4250:
                CD = 0.599022D - 0.0134303D * v1 + 2.53003E-4D * v2 - 3.79232E-6D * v3 + 3.93889E-8D * v4 - 1.71003E-10D * v5 - 1.10566E-14D * v6;
                break;
            case 4500:
                CD = 0.603394D - 0.0131947D * v1 + 2.36469E-4D * v2 - 3.26544E-6D * v3 + 3.17053E-8D * v4 - 1.20565E-10D * v5 - 1.26398E-13D * v6;
                break;
            case 4750:
                CD = 0.636413D - 0.0155299D * v1 + 3.60004E-4D * v2 - 7.0931E-6D * v3 + 9.71212E-8D * v4 - 6.9967E-10D * v5 + 1.95333E-12D * v6;
                break;
            case 5000:
                CD = 0.669496D - 0.0178723D * v1 + 4.8382E-4D * v2 - 1.09258E-5D * v3 + 1.6258E-7D * v4 - 1.27892E-9D * v5 + 4.0331E-12D * v6;
                break;
            case 5250:
                CD = 0.67579D - 0.0178792D * v1 + 4.84231E-4D * v2 - 1.09376E-5D * v3 + 1.62758E-7D * v4 - 1.28026E-9D * v5 + 4.03706E-12D * v6;
                break;
            case 5500:
                CD = 0.682025D - 0.0178769D * v1 + 4.84088E-4D * v2 - 1.09334E-5D * v3 + 1.62692E-7D * v4 - 1.27975E-9D * v5 + 4.03554E-12D * v6;
                break;
            case 5750:
                CD = 0.688273D - 0.0178764D * v1 + 4.84056E-4D * v2 - 1.09323E-5D * v3 + 1.62673E-7D * v4 - 1.27959E-9D * v5 + 4.03499E-12D * v6;
                break;
            case 6000:
                CD = 0.694496D - 0.0178723D * v1 + 4.8382E-4D * v2 - 1.09258E-5D * v3 + 1.6258E-7D * v4 - 1.27892E-9D * v5 + 4.0331E-12D * v6;
                break;
            case 6250:
                CD = 0.700272D - 0.0178761D * v1 + 4.84032E-4D * v2 - 1.09314E-5D * v3 + 1.62658E-7D * v4 - 1.27946E-9D * v5 + 4.03458E-12D * v6;
                break;
            case 6500:
                CD = 0.706022D - 0.0178761D * v1 + 4.84032E-4D * v2 - 1.09314E-5D * v3 + 1.62658E-7D * v4 - 1.27946E-9D * v5 + 4.03458E-12D * v6;
                break;
            case 6750:
                CD = 0.711802D - 0.017881D * v1 + 4.84321E-4D * v2 - 1.09399E-5D * v3 + 1.62787E-7D * v4 - 1.28044E-9D * v5 + 4.03751E-12D * v6;
                break;
            case 7000:
                CD = 0.717496D - 0.0178723D * v1 + 4.8382E-4D * v2 - 1.09258E-5D * v3 + 1.6258E-7D * v4 - 1.27892E-9D * v5 + 4.0331E-12D * v6;
                break;
            case 7250:
                CD = 0.695509D - 0.0155412D * v1 + 3.60529E-4D * v2 - 7.10545E-6D * v3 + 9.72769E-8D * v4 - 7.00673E-10D * v5 + 1.95592E-12D * v6;
                break;
            case 7500:
                CD = 0.673544D - 0.0132138D * v1 + 2.37411E-4D * v2 - 3.28866E-6D * v3 + 3.20088E-8D * v4 - 1.22574E-10D * v5 - 1.21112E-13D * v6;
                break;
            case 7750:
                CD = 0.651374D - 0.0108573D * v1 + 1.12722E-4D * v2 + 5.70074E-7D * v3 - 3.38509E-8D * v4 + 4.59742E-10D * v5 - 2.21008E-12D * v6;
                break;
            case 8000:
                CD = 0.629352D - 0.00852307D * v1 - 1.06786E-5D * v2 + 4.39219E-6D * v3 - 9.91653E-8D * v4 + 1.03799E-9D * v5 - 4.2871E-12D * v6;
                break;
            case 8250:
                CD = 0.632646D - 0.00852891D * v1 - 1.03728E-5D * v2 + 4.38425E-6D * v3 - 9.90567E-8D * v4 + 1.03724E-9D * v5 - 4.28505E-12D * v6;
                break;
            case 8500:
                CD = 0.635909D - 0.00853094D * v1 - 1.02499E-5D * v2 + 4.38057E-6D * v3 - 9.89993E-8D * v4 + 1.03679E-9D * v5 - 4.28367E-12D * v6;
                break;
            case 8750:
                CD = 0.639159D - 0.00853094D * v1 - 1.02499E-5D * v2 + 4.38057E-6D * v3 - 9.89993E-8D * v4 + 1.03679E-9D * v5 - 4.28367E-12D * v6;
                break;
            case 9000:
                CD = 0.642352D - 0.00852307D * v1 - 1.06786E-5D * v2 + 4.39219E-6D * v3 - 9.91653E-8D * v4 + 1.03799E-9D * v5 - 4.2871E-12D * v6;
                break;
            case 9250:
                CD = 0.646154D - 0.00853016D * v1 - 1.0304E-5D * v2 + 4.38236E-6D * v3 - 9.90289E-8D * v4 + 1.03703E-9D * v5 - 4.2844E-12D * v6;
                break;
            case 9500:
                CD = 0.649895D - 0.00852889D * v1 - 1.03671E-5D * v2 + 4.38383E-6D * v3 - 9.90461E-8D * v4 + 1.03713E-9D * v5 - 4.28461E-12D * v6;
                break;
            case 9750:
                CD = 0.653632D - 0.00852686D * v1 - 1.049E-5D * v2 + 4.3875E-6D * v3 - 9.91035E-8D * v4 + 1.03758E-9D * v5 - 4.28599E-12D * v6;
                break;
            case 10000:
                CD = 0.657352D - 0.00852307D * v1 - 1.06786E-5D * v2 + 4.39219E-6D * v3 - 9.91653E-8D * v4 + 1.03799E-9D * v5 - 4.2871E-12D * v6;
                break;
            case 10250:
                CD = 0.659882D - 0.00852686D * v1 - 1.049E-5D * v2 + 4.3875E-6D * v3 - 9.91035E-8D * v4 + 1.03758E-9D * v5 - 4.28599E-12D * v6;
                break;
            case 10500:
                CD = 0.662382D - 0.00852686D * v1 - 1.049E-5D * v2 + 4.3875E-6D * v3 - 9.91035E-8D * v4 + 1.03758E-9D * v5 - 4.28599E-12D * v6;
                break;
            case 10750:
                CD = 0.664882D - 0.00852686D * v1 - 1.049E-5D * v2 + 4.3875E-6D * v3 - 9.91035E-8D * v4 + 1.03758E-9D * v5 - 4.28599E-12D * v6;
                break;
            case 11000:
                CD = 0.667352D - 0.00852307D * v1 - 1.06786E-5D * v2 + 4.39219E-6D * v3 - 9.91653E-8D * v4 + 1.03799E-9D * v5 - 4.2871E-12D * v6;
                break;
            case 11250:
                CD = 0.672345D - 0.00852103D * v1 - 1.08349E-5D * v2 + 4.39731E-6D * v3 - 9.92479E-8D * v4 + 1.03864E-9D * v5 - 4.28905E-12D * v6;
                break;
            case 11500:
                CD = 0.677355D - 0.0085223D * v1 - 1.07718E-5D * v2 + 4.39585E-6D * v3 - 9.92307E-8D * v4 + 1.03854E-9D * v5 - 4.28883E-12D * v6;
                break;
            case 11750:
                CD = 0.682355D - 0.0085223D * v1 - 1.07718E-5D * v2 + 4.39585E-6D * v3 - 9.92307E-8D * v4 + 1.03854E-9D * v5 - 4.28883E-12D * v6;
                break;
            case 12000:
                CD = 0.687352D - 0.00852307D * v1 - 1.06786E-5D * v2 + 4.39219E-6D * v3 - 9.91653E-8D * v4 + 1.03799E-9D * v5 - 4.2871E-12D * v6;
                break;
            case 12250:
                CD = 0.708813D - 0.0108663D * v1 + 1.13225E-4D * v2 + 5.56328E-7D * v3 - 3.36543E-8D * v4 + 4.58339E-10D * v5 - 2.20615E-12D * v6;
                break;
            case 12500:
                CD = 0.730247D - 0.0132076D * v1 + 2.37099E-4D * v2 - 3.28108E-6D * v3 + 3.19131E-8D * v4 - 1.21966E-10D * v5 - 1.22641E-13D * v6;
                break;
            case 12750:
                CD = 0.751683D - 0.0155485D * v1 + 3.60923E-4D * v2 - 7.11565E-6D * v3 + 9.74145E-8D * v4 - 7.01591E-10D * v5 + 1.95833E-12D * v6;
                break;
            case 13000:
                CD = 0.773038D - 0.0178787D * v1 + 4.84191E-4D * v2 - 1.09362E-5D * v3 + 1.62733E-7D * v4 - 1.28004E-9D * v5 + 4.03634E-12D * v6;
                break;
            case 13250:
                CD = 0.776171D - 0.0178812D * v1 + 4.84411E-4D * v2 - 1.09442E-5D * v3 + 1.6287E-7D * v4 - 1.28115E-9D * v5 + 4.03972E-12D * v6;
                break;
            case 13500:
                CD = 0.778303D - 0.0178728D * v1 + 4.83926E-4D * v2 - 1.09306E-5D * v3 + 1.62674E-7D * v4 - 1.27974E-9D * v5 + 4.03582E-12D * v6;
                break;
            case 13750:
                CD = 0.780496D - 0.0178723D * v1 + 4.8382E-4D * v2 - 1.09258E-5D * v3 + 1.6258E-7D * v4 - 1.27892E-9D * v5 + 4.0331E-12D * v6;
                break;
            default:
                CD = 0.0D;
        }

        return CD;
    }

    private double KHG_CL(double v1, double v2, double v3, double v4, double v5, double v6, int spin) {
        double CL;
        switch(spin) {
            case 500:
                CL = -0.154822D - 0.0140245D * v1 + 0.00301665D * v2 - 1.26638E-4D * v3 + 2.266E-6D * v4 - 1.86851E-8D * v5 + 5.83844E-11D * v6;
                break;
            case 750:
                CL = -0.150822D - 0.0140245D * v1 + 0.00301665D * v2 - 1.26638E-4D * v3 + 2.266E-6D * v4 - 1.86851E-8D * v5 + 5.83844E-11D * v6;
                break;
            case 1000:
                CL = -0.0840199D - 0.0130903D * v1 + 0.00269051D * v2 - 1.12903E-4D * v3 + 2.02046E-6D * v4 - 1.66615E-8D * v5 + 5.20772E-11D * v6;
                break;
            case 1250:
                CL = -0.01521464D - 0.0121566D * v1 + 0.00236439D * v2 - 9.91687E-5D * v3 + 1.77493E-6D * v4 - 1.4638E-8D * v5 + 4.57703E-11D * v6;
                break;
            case 1500:
                CL = 0.0535987D - 0.011225D * v1 + 0.00203843D * v2 - 8.54395E-5D * v3 + 1.52948E-6D * v4 - 1.26151E-8D * v5 + 3.94651E-11D * v6;
                break;
            case 1750:
                CL = 0.116377D - 0.0102875D * v1 + 0.0017121D * v2 - 7.16991E-5D * v3 + 1.28385E-6D * v4 - 1.05908E-8D * v5 + 3.31558E-11D * v6;
                break;
            case 2000:
                CL = 0.179264D - 0.00936465D * v1 + 0.00138658D * v2 - 5.79814E-5D * v3 + 1.03856E-6D * v4 - 8.56907E-9D * v5 + 2.68546E-11D * v6;
                break;
            case 2250:
                CL = 0.241996D - 0.00841981D * v1 + 0.00105981D * v2 - 4.42284E-5D * v3 + 7.92732E-7D * v4 - 6.54331E-9D * v5 + 2.05411E-11D * v6;
                break;
            case 2500:
                CL = 0.304749D - 0.00747834D * v1 + 7.33251E-4D * v2 - 3.04817E-5D * v3 + 5.47008E-7D * v4 - 4.51836E-9D * v5 + 1.42301E-11D * v6;
                break;
            case 2750:
                CL = 0.367526D - 0.00653979D * v1 + 4.06879E-4D * v2 - 1.67413E-5D * v3 + 3.01394E-7D * v4 - 2.49432E-9D * v5 + 7.92207E-12D * v6;
                break;
            case 3000:
                CL = 0.4303D - 0.00560158D * v1 + 8.05219E-5D * v2 - 3.00061E-6D * v3 + 5.57649E-8D * v4 - 4.70085E-10D * v5 + 1.61326E-12D * v6;
                break;
            case 3250:
                CL = 0.433383D - 0.00540762D * v1 + 8.51563E-5D * v2 - 3.31016E-6D * v3 + 6.0718E-8D * v4 - 4.97869E-10D * v5 + 1.64195E-12D * v6;
                break;
            case 3500:
                CL = 0.436671D - 0.00524399D * v1 + 9.14933E-5D * v2 - 3.66678E-6D * v3 + 6.63572E-8D * v4 - 5.30701E-10D * v5 + 1.68537E-12D * v6;
                break;
            case 3750:
                CL = 0.439774D - 0.00505329D * v1 + 9.6331E-5D * v2 - 3.98245E-6D * v3 + 7.14045E-8D * v4 - 5.59199E-10D * v5 + 1.71616E-12D * v6;
                break;
            case 4000:
                CL = 0.442902D - 0.00486613D * v1 + 1.01355E-4D * v2 - 4.30317E-6D * v3 + 7.65279E-8D * v4 - 5.88296E-10D * v5 + 1.74886E-12D * v6;
                break;
            case 4250:
                CL = 0.446157D - 0.00469729D * v1 + 1.07418E-4D * v2 - 4.65309E-6D * v3 + 8.20806E-8D * v4 - 6.2055E-10D * v5 + 1.7907E-12D * v6;
                break;
            case 4500:
                CL = 0.449305D - 0.00451328D * v1 + 1.12632E-4D * v2 - 4.97943E-6D * v3 + 8.72895E-8D * v4 - 6.50286E-10D * v5 + 1.82525E-12D * v6;
                break;
            case 4750:
                CL = 0.468931D - 0.0048215D * v1 + 9.67031E-5D * v2 - 4.2306E-6D * v3 + 7.87939E-8D * v4 - 6.48735E-10D * v5 + 2.10142E-12D * v6;
                break;
            case 5000:
                CL = 0.488425D - 0.00511167D * v1 + 7.98252E-5D * v2 - 3.45726E-6D * v3 + 6.99643E-8D * v4 - 6.44875E-10D * v5 + 2.37119E-12D * v6;
                break;
            case 5250:
                CL = 0.495927D - 0.00511214D * v1 + 7.98614E-5D * v2 - 3.45847E-6D * v3 + 6.99842E-8D * v4 - 6.45035E-10D * v5 + 2.37169E-12D * v6;
                break;
            case 5500:
                CL = 0.503412D - 0.00511002D * v1 + 7.97472E-5D * v2 - 3.45552E-6D * v3 + 6.99448E-8D * v4 - 6.44771E-10D * v5 + 2.37099E-12D * v6;
                break;
            case 5750:
                CL = 0.510888D - 0.00510664D * v1 + 7.9574E-5D * v2 - 3.45117E-6D * v3 + 6.9887E-8D * v4 - 6.44383E-10D * v5 + 2.36995E-12D * v6;
                break;
            case 6000:
                CL = 0.518425D - 0.00511167D * v1 + 7.98252E-5D * v2 - 3.45726E-6D * v3 + 6.99643E-8D * v4 - 6.44875E-10D * v5 + 2.37119E-12D * v6;
                break;
            case 6250:
                CL = 0.525155D - 0.00510921D * v1 + 7.97245E-5D * v2 - 3.45543E-6D * v3 + 6.99493E-8D * v4 - 6.44836E-10D * v5 + 2.37125E-12D * v6;
                break;
            case 6500:
                CL = 0.531905D - 0.00510921D * v1 + 7.97245E-5D * v2 - 3.45543E-6D * v3 + 6.99493E-8D * v4 - 6.44836E-10D * v5 + 2.37125E-12D * v6;
                break;
            case 6750:
                CL = 0.538653D - 0.00510876D * v1 + 7.96882E-5D * v2 - 3.45412E-6D * v3 + 6.99265E-8D * v4 - 6.44647E-10D * v5 + 2.37065E-12D * v6;
                break;
            case 7000:
                CL = 0.545425D - 0.00511167D * v1 + 7.98252E-5D * v2 - 3.45726E-6D * v3 + 6.99643E-8D * v4 - 6.44875E-10D * v5 + 2.37119E-12D * v6;
                break;
            case 7250:
                CL = 0.544578D - 0.00480758D * v1 + 9.59787E-5D * v2 - 4.21176E-6D * v3 + 7.85303E-8D * v4 - 6.46838E-10D * v5 + 2.09591E-12D * v6;
                break;
            case 7500:
                CL = 0.543714D - 0.00450004D * v1 + 1.11891E-4D * v2 - 4.95872E-6D * v3 + 8.69802E-8D * v4 - 6.47935E-10D * v5 + 1.81814E-12D * v6;
                break;
            case 7750:
                CL = 0.542964D - 0.00421025D * v1 + 1.28852E-4D * v2 - 5.73584E-6D * v3 + 9.58824E-8D * v4 - 6.52428E-10D * v5 + 1.55042E-12D * v6;
                break;
            case 8000:
                CL = 0.542118D - 0.00390649D * v1 + 1.45041E-4D * v2 - 6.49224E-6D * v3 + 1.04495E-7D * v4 - 6.54894E-10D * v5 + 1.2771E-12D * v6;
                break;
            case 8250:
                CL = 0.545392D - 0.00390896D * v1 + 1.45121E-4D * v2 - 6.49286E-6D * v3 + 1.04481E-7D * v4 - 6.54638E-10D * v5 + 1.27593E-12D * v6;
                break;
            case 8500:
                CL = 0.548679D - 0.0039144D * v1 + 1.45421E-4D * v2 - 6.50102E-6D * v3 + 1.04599E-7D * v4 - 6.55492E-10D * v5 + 1.27841E-12D * v6;
                break;
            case 8750:
                CL = 0.551929D - 0.0039144D * v1 + 1.45421E-4D * v2 - 6.50102E-6D * v3 + 1.04599E-7D * v4 - 6.55492E-10D * v5 + 1.27841E-12D * v6;
                break;
            case 9000:
                CL = 0.555118D - 0.00390649D * v1 + 1.45041E-4D * v2 - 6.49224E-6D * v3 + 1.04495E-7D * v4 - 6.54894E-10D * v5 + 1.2771E-12D * v6;
                break;
            case 9250:
                CL = 0.559402D - 0.00391117D * v1 + 1.45266E-4D * v2 - 6.49731E-6D * v3 + 1.04552E-7D * v4 - 6.55187E-10D * v5 + 1.27762E-12D * v6;
                break;
            case 9500:
                CL = 0.563697D - 0.00391692D * v1 + 1.45554E-4D * v2 - 6.50453E-6D * v3 + 1.04648E-7D * v4 - 6.55843E-10D * v5 + 1.27939E-12D * v6;
                break;
            case 9750:
                CL = 0.567899D - 0.00390971D * v1 + 1.45154E-4D * v2 - 6.49365E-6D * v3 + 1.04493E-7D * v4 - 6.54721E-10D * v5 + 1.27619E-12D * v6;
                break;
            case 10000:
                CL = 0.572118D - 0.00390649D * v1 + 1.45041E-4D * v2 - 6.49224E-6D * v3 + 1.04495E-7D * v4 - 6.54894E-10D * v5 + 1.2771E-12D * v6;
                break;
            case 10250:
                CL = 0.574646D - 0.00390946D * v1 + 1.45151E-4D * v2 - 6.49394E-6D * v3 + 1.04503E-7D * v4 - 6.54847E-10D * v5 + 1.27668E-12D * v6;
                break;
            case 10500:
                CL = 0.577146D - 0.00390946D * v1 + 1.45151E-4D * v2 - 6.49394E-6D * v3 + 1.04503E-7D * v4 - 6.54847E-10D * v5 + 1.27668E-12D * v6;
                break;
            case 10750:
                CL = 0.579648D - 0.00391006D * v1 + 1.45202E-4D * v2 - 6.49577E-6D * v3 + 1.04535E-7D * v4 - 6.55105E-10D * v5 + 1.2775E-12D * v6;
                break;
            case 11000:
                CL = 0.582118D - 0.00390649D * v1 + 1.45041E-4D * v2 - 6.49224E-6D * v3 + 1.04495E-7D * v4 - 6.54894E-10D * v5 + 1.2771E-12D * v6;
                break;
            case 11250:
                CL = 0.582118D - 0.00390649D * v1 + 1.45041E-4D * v2 - 6.49224E-6D * v3 + 1.04495E-7D * v4 - 6.54894E-10D * v5 + 1.2771E-12D * v6;
                break;
            case 11500:
                CL = 0.582118D - 0.00390649D * v1 + 1.45041E-4D * v2 - 6.49224E-6D * v3 + 1.04495E-7D * v4 - 6.54894E-10D * v5 + 1.2771E-12D * v6;
                break;
            case 11750:
                CL = 0.582118D - 0.00390649D * v1 + 1.45041E-4D * v2 - 6.49224E-6D * v3 + 1.04495E-7D * v4 - 6.54894E-10D * v5 + 1.2771E-12D * v6;
                break;
            case 12000:
                CL = 0.582118D - 0.00390649D * v1 + 1.45041E-4D * v2 - 6.49224E-6D * v3 + 1.04495E-7D * v4 - 6.54894E-10D * v5 + 1.2771E-12D * v6;
                break;
            case 12250:
                CL = 0.588431D - 0.00420519D * v1 + 1.2857E-4D * v2 - 5.72855E-6D * v3 + 9.57887E-8D * v4 - 6.51851E-10D * v5 + 1.54908E-12D * v6;
                break;
            case 12500:
                CL = 0.594759D - 0.00450723D * v1 + 1.12329E-4D * v2 - 4.97178E-6D * v3 + 8.71841E-8D * v4 - 6.49526E-10D * v5 + 1.82302E-12D * v6;
                break;
            case 12750:
                CL = 0.601034D - 0.00480116D * v1 + 9.56459E-5D * v2 - 4.20349E-6D * v3 + 7.84238E-8D * v4 - 6.46156E-10D * v5 + 2.09421E-12D * v6;
                break;
            case 13000:
                CL = 0.607415D - 0.00511026D * v1 + 7.97466E-5D * v2 - 3.45501E-6D * v3 + 6.99293E-8D * v4 - 6.44597E-10D * v5 + 2.37032E-12D * v6;
                break;
            case 13250:
                CL = 0.600653D - 0.00510876D * v1 + 7.96882E-5D * v2 - 3.45412E-6D * v3 + 6.99265E-8D * v4 - 6.44647E-10D * v5 + 2.37065E-12D * v6;
                break;
            case 13500:
                CL = 0.600522D - 0.00510806D * v1 + 7.96734E-5D * v2 - 3.4545E-6D * v3 + 6.99425E-8D * v4 - 6.44828E-10D * v5 + 2.37132E-12D * v6;
                break;
            case 13750:
                CL = 0.600425D - 0.00511167D * v1 + 7.98252E-5D * v2 - 3.45726E-6D * v3 + 6.99643E-8D * v4 - 6.44875E-10D * v5 + 2.37119E-12D * v6;
                break;
            default:
                CL = 0.0D;
        }

        return CL;
    }
}
