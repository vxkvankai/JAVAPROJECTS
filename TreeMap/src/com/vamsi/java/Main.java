package com.vamsi.java;

import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

class Code implements Comparable<Code> {
    private String setctionid;
    private String lectureNo;

    public Code(String setctionid, String lectureNo)
    {
        this.setctionid = setctionid;
        this.lectureNo = lectureNo;
    }

    public String getSetctionid()
    {
        return setctionid;
    }

    public String getLectureNo()
    {
        return lectureNo;
    }

    @Override
    public String toString()
    {
        return "Code{" +
                "setctionid='" + setctionid + '\'' +
                ", lectureNo='" + lectureNo + '\'' +
                '}';
    }

    @Override
    public int compareTo(Code o)
    {

        String code1 = setctionid.concat(lectureNo);
        String code2 = o.getSetctionid() + o.getLectureNo();
        return code1.compareTo(code2);

    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o)
        {
            return true;
        }
        if (!(o instanceof Code))
        {
            return false;
        }
        Code code = (Code) o;
        return Objects.equals(getSetctionid(), code.getSetctionid()) &&
                Objects.equals(getLectureNo(), code.getLectureNo());
    }

    @Override
    public int hashCode()
    {

        return Objects.hash(getSetctionid(), getLectureNo());
    }
}

public class Main {

    public static void main(String[] args)
    {
        Map<Code, String> lectures = new TreeMap<>();
        lectures.put(new Code("S01", "L03"), "Generics");
        lectures.put(new Code("S01", "L01"), "Files under Java");
        lectures.put(new Code("S02", "L03"), "Network Programming");
        lectures.put(new Code("S01", "L07"), "OOPs");
        lectures.put(new Code("S01", "L05"), "Methods");
        lectures.put(new Code("S01", "L03"), "Expressions");


        for (Map.Entry<Code, String> entry : lectures.entrySet())
        {
            // System.out.println("Key: " + entry.getKey() + " Value: " + entry.getValue());
        }


    }
}
