package com.zhouwei.springboot.test;

import com.zhouwei.springboot.entity.SjTaskDetail;
import com.zhouwei.springboot.entity.SjTaskGjcList;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class M {

    public static void main(String[] args) {
//        HashMap mp=new HashMap();
//        mp.put("A","A");
//        mp.put("B","B");
//        mp.put("C","C");
//        System.out.println(mp.get("A"));
//       Iterator list=mp.entrySet().iterator();
//        while(list.hasNext()){
//            Map.Entry  p=(Map.Entry) list.next();
//            System.out.println(p.getKey());
//        }

        SjTaskGjcList s1=new SjTaskGjcList(123,100,"中国",100,100,"2020-05-05",null,null,null,null,null);
        SjTaskGjcList s2=new SjTaskGjcList(124,100,"中国",200,200,"2020-05-05",null,null,null,null,null);
        SjTaskGjcList s3=new SjTaskGjcList(125,100,"宋朝",200,200,"2020-05-05",null,null,null,null,null);

        List<SjTaskGjcList> lg=Arrays.asList(s1,s2,s3);
////        Random random = new Random();
////        random.ints().limit(10).sorted().forEach(System.out::println);
//        Collections.shuffle(lg);
//        lg.stream().forEach(System.out::println);
//        //tolist();
//        List<Integer> ltt= Arrays.asList(6,3,7,2);
//        ltt=ltt.stream().sorted().collect(Collectors.toList());
//        List<String> list= lg.stream().map(SjTaskGjcList:: getGjc).distinct().collect(Collectors.toList());
       List<SjTaskGjcList> list=lg.stream().collect(Collectors.groupingBy(obj->obj.getGjc()))
               .entrySet().stream().
                       filter(entry->entry.getValue().size()>1)
               .map(entry->entry.getValue().get(0))
               .collect(Collectors.toList());
       // System.out.println(list.get(0).getGjc()+"="+list.get(0).getXnumber());
        List<SjTaskGjcList> listt=lg.stream().collect(Collectors.groupingBy(obj->obj.getGjc()))
                .entrySet().stream().
                        filter(entry->entry.getValue().size()<=1)
                .map(entry->entry.getValue().get(0))
                .collect(Collectors.toList());
        List lkk=Stream.of(list,listt).collect(Collectors.toList());

       System.out.println(lkk);
    }

    public static  void tolist(){
        List<SjTaskGjcList> lg=new ArrayList<SjTaskGjcList>();
        SjTaskGjcList s1=new SjTaskGjcList(123,100,"中国",100,100,"2020-05-05",null,null,null,null,null);
        SjTaskGjcList s2=new SjTaskGjcList(124,100,"历史",200,200,"2020-05-05",null,null,null,null,null);
        lg.add(s1);
        lg.add(s2);

        SjTaskDetail d1=new SjTaskDetail(123+"",99+"");
        SjTaskDetail d2=new SjTaskDetail(124+"",100+"");
        List<SjTaskDetail> ld=Arrays.asList(d1,d2);
        List list3=lg.stream().map(
            g -> {List<SjTaskDetail> res=ld.stream().
                       filter(d -> g.getId() == Integer.parseInt(d.getR3())
                               && d.getR5()!=null&&
                               g.getXnumber() > Integer.parseInt(d.getR5()))
                    .collect(Collectors.toList());
                if(res.size()>0) {
                    return g;
                }else{
                    return null;
                }
            }
           ).filter(Objects::nonNull)
                .collect(Collectors.toList());
                //.map(g-> new  SjTaskGjcList(g.getId(),g.getTaskid(),g.getGjc(),g.getXnumber(),g.getSjnumber(),g.getChangetime(),null,null,null,null,null))


        for(int i=0;i<list3.size();i++){
           SjTaskGjcList sjTaskGjcList=(SjTaskGjcList) list3.get(i);
           System.out.println(list3.get(i).toString());
        }

//       String s=lg.stream().map(
//                g -> {List<SjTaskDetail> res=ld.stream().
//                        filter(d -> g.getId() == Integer.parseInt(d.getR3())
//                                && d.getR5()!=null&&
//                                g.getXnumber() > Integer.parseInt(d.getR5()))
//                        .collect(Collectors.toList());
//                    if(res.size()>0) {
//                        return g.getGjc();
//                    }else{
//                        return null;
//                    }
//                }
//        ).filter(Objects::nonNull).collect(Collectors.joining(","));
//               //reduce("",g -> ","+g);
//        System.out.println(s);
//       String ss= lg.stream().map(g ->
//            {boolean b=g.getGjc().isEmpty();
//                if(b){
//                    return null;
//                }else{
//                    return g.getGjc();
//                }
//
//        }).filter(Objects::nonNull).collect(Collectors.joining(","));
//        System.out.println(ss);

        IntSummaryStatistics s=   lg.stream().mapToInt(
                g->{List b=ld.stream()
                        .filter(d ->
                                Integer.parseInt(d.getR5())>=g.getXnumber()).collect(Collectors.toList());
                    if (b!=null&&b.size()>0){
                        return g.getXnumber();
                    }else{
                        return 0;
                    }
                }
        ).filter(Objects::nonNull).summaryStatistics();
        System.out.println(s.getSum());
    }
}
