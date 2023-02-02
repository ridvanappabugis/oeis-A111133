package algo.bigint;



import java.util.*;

/**
 * Bare positive-only BigInt class based on Simon Klein's BigInt.
 */
public class BigInt implements Comparable<BigInt>
{
    private static final long mask = (1L<<32) - 1;
    private static final int sign = 1;
    public int len;
    private int[] dig;

    public BigInt(final int[] v, final int len) {
        assign(v,len);
    }

    public BigInt(final int val) {
        dig = new int[1];
        uassign(val);
    }

    private void realloc() {
        final int[] res = new int[dig.length*2];
        System.arraycopy(dig,0,res,0,len);
        dig = res;
    }

    private void realloc(final int newLen) {
        final int[] res = new int[newLen];
        System.arraycopy(dig,0,res,0,len);
        dig = res;
    }

    public BigInt copy() {
        return new BigInt(Arrays.copyOf(dig,len), len);
    }

    public void assign(final int[] v, final int len) {
        this.len = len;
        dig = v;
    }

    public void uassign(final int val) {
        len = 1;
        dig[0] = val;
    }

    public boolean isZero() {
        return len==1 && dig[0]==0;
    }
    /**
     * Increases the magnitude of this number by the given magnitude array.
     *
     * @param v		The magnitude array of the increase.
     * @param vlen	The length (number of digits) of the increase.
     * @complexity	O(n)
     */
    private void addMag(int[] v, int vlen) {
        int ulen = len;
        int[] u = dig; //ulen <= vlen
        if(vlen<ulen){ u = v; v = dig; ulen = vlen; vlen = len; }
        if(vlen>dig.length) realloc(vlen+1);

        long carry = 0; int i = 0;
        for(; i<ulen; i++)
        {
            carry = (u[i]&mask) + (v[i]&mask) + carry;
            dig[i] = (int)carry;
            carry >>>= 32;
        }
        if(vlen>len){ System.arraycopy(v, len, dig, len, vlen-len); len = vlen; }
        if(carry!=0) //carry==1
        {
            for(; i<len && ++dig[i]==0; i++);
            if(i==len) //vlen==len
            {
                if(len==dig.length) realloc();
                dig[len++] = 1;
            }
        }
    }

    public BigInt add(final BigInt a) {
        addMag(a.dig,a.len);
        return this;
    }

    public BigInt copyAdd(final BigInt a){
        return this.copy().add(a);
    }

    public String toString() {
        if(isZero()) return "0";

        int top = len*10 + 1;
        final char[] buf = new char[top];
        Arrays.fill(buf, '0');
        final int[] cpy = Arrays.copyOf(dig,len);
        while(true)
        {
            final int j = top;
            for(long tmp = toStringDiv(); tmp>0; tmp/=10)
                buf[--top] += tmp%10; //TODO: Optimize.
            if(len==1 && dig[0]==0) break;
            else top = j-13;
        }
        if(sign<0) buf[--top] = '-';
        System.arraycopy(cpy,0,dig,0, len = cpy.length);
        return new String(buf, top, buf.length-top);
    }
    // Divides the number by 10^13 and returns the remainder.
    // Does not change the sign of the number.
    private long toStringDiv() {
        final int pow5 = 1_220_703_125, pow2 = 1<<13;
        int nextq = 0;
        long rem = 0;
        for(int i = len-1; i>0; i--)
        {
            rem = (rem<<32) + (dig[i]&mask);
            final int q = (int)(rem/pow5);
            rem = rem%pow5;
            dig[i] = nextq|q>>>13;
            nextq = q<<32-13;
        }
        rem = (rem<<32) + (dig[0]&mask);
        final int mod2 = dig[0]&pow2-1;
        dig[0] = nextq|(int)(rem/pow5 >>> 13);
        rem = rem%pow5;
        // Applies the Chinese Remainder Theorem.
        // -67*5^13 + 9983778*2^13 = 1
        final long pow10 = (long)pow5*pow2;
        rem = (rem - pow5*(mod2 - rem)%pow10*67)%pow10;
        if(rem<0) rem += pow10;
        if(dig[len-1]==0 && len>1)
            if(dig[--len-1]==0 && len>1)
                --len;
        return rem;
    }


    public int compareAbsTo(final BigInt a)
    {
        if(len>a.len) return 1;
        if(len<a.len) return -1;
        for(int i = len-1; i>=0; i--)
            if(dig[i]!=a.dig[i])
                if((dig[i]&mask)>(a.dig[i]&mask)) return 1;
                else return -1;
        return 0;
    }

    @Override
    public int compareTo(final BigInt a)
    {
        if(sign>0 || a.isZero()) return compareAbsTo(a);
        return 1;
    }

    public boolean equals(final BigInt a) {
        if(len!=a.len) return false;
        if(isZero() && a.isZero()) return true;
        for(int i = 0; i<len; i++) if(dig[i]!=a.dig[i]) return false;
        return true;
    }

    @Override
    public boolean equals(final Object o) {
        if(o instanceof BigInt) return equals((BigInt)o);
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 0; //Todo: Opt and improve.
        for(int i = 0; i<len; i++) hash = (int)(31*hash + (dig[i]&mask));
        return sign*hash; //relies on 0 --> hash==0.
    }

}